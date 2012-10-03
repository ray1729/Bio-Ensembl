(ns ensembl.core
  (:use [ensembl.config :only (data-source)])
  (:import [uk.ac.roslin.ensembl.dao.database DBRegistry]))

(def ^:dynamic *registry* nil)
(def ^:dynamic *species* nil)

(defmacro with-registry
  [registry & body]
  `(binding [*registry* ~registry] ~@body))

(defmacro with-species
  [species & body]
  `(binding [*species* ~species] ~@body))

(defn registry
  [ds]
  (DBRegistry. (data-source ds)))

(defn list-species
  [& {:keys [registry] :or {registry *registry*}}]
  (map (memfn getDatabaseStyleName) (.getSpecies registry)))

(defn species
  [s & {:keys [registry] :or {registry *registry*}}]
  (or (.getSpeciesByEnsemblName registry (name s))
      (.getSpeciesByAlias registry (name s))))

(defn list-chromosomes
  [s]
  (map key (.getChromosomes s)))

(defn chromosome
  [n & {:keys [species] :or {species *species*}}]
  (.getChromosomeByName (ensembl.core/species species) n))
