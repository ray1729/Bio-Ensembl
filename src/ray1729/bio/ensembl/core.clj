(ns ray1729.bio.ensembl.core
  (:refer-clojure
   :exclude [take drop sort distinct conj! disj! compile case])
  (:use
   [clojure.contrib.sql :only (with-connection)]
   [clojureql.core]))

(def *ensembldb-host* "ensembldb.ensembl.org")
(def *ensembldb-port* 5306)
(def *species-id* 1)

(defn ensembldb
  [dbname]
  {:classname   "com.mysql.jdbc.Driver"
   :subprotocol "mysql"
   :user        "anonymous"
   :subname     (format "//%s:%s/%s" *ensembldb-host* *ensembldb-port* dbname)
   :auto-commit true
   :fetch-size  500})

(def mousedb (ensembldb "mus_musculus_core_61_37n"))

;;(open-global :mousedb mousedb)

(defn fetch-gene-by-stable-id
  [id]
  (-> (table :gene_stable_id)
      (select (where (= :stable_id id)))
      (join (table :gene) :gene_id)
      (project [:gene.* :gene_stable_id.stable_id])))

(defn fetch-gene-by-external-name
  [name]
  (let [base-query (-> (table :gene)
                       (select (where (= :gene.is_current 1)))
                       (join (table :gene_stable_id) :gene_id)
                       (join (table :seq_region)
                             (where (= :gene.seq_region_id :seq_region.seq_region_id)))
                       (join (table :coord_system)
                             (where (and (= :coord_system.coord_system_id :seq_region.coord_system_id)
                                         (= :coord_system.species_id *species-id*))))
                       (join (table :object_xref)
                             (where (and (= :object_xref.ensembl_id :gene.gene_id)
                                         (= :object_xref.ensembl_object_type "Gene"))))
                       (join (table :xref)
                             (where (= :xref.xref_id :object_xref.xref_id))))
        query1 (-> base-query
                   (select (where (or (= :xref.dbprimary_acc name)
                                      (= :xref.display_label name))))
                   (project [:gene.*, :gene_stable_id.stable_id])
                   (distinct))
        query2 (-> base-query
                   (join (table :external_synonym)
                         (where (= :external_synonym.xref_id :object_xref.xref_id)))
                   (select (where (= :external_synonym.synonym name)))
                   (project [:gene.*, :gene_stable_id.stable_id])
                   (distinct))]
    (union query1 query2)))

;; XXX Throws exception "No value specified for parameter 7"
;; (with-connection mousedb @(fetch-gene-by-external-name "Cbx1"))

