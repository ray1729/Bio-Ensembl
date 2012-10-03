(defproject ensembl "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [uk.ac.roslin/ensembl-data-access "1.13"]
                 [uk.ac.roslin/ensembl-config "1.68"]]
  :repositories [["jensembl" {:url "http://jensembl.sourceforge.net/m2-repo" :checksum :ignore :snapshots false }]
                 ["biojava" {:url "http://www.biojava.org/download/maven/" :checksum :ignore :snapshots false }]])
