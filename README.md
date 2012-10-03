# ensembl

An experimental Clojure API wrapping the excellent Java Ensembl
library JEnsembl, <http://jensembl.sourceforge.net/>.

This is *alpha* software: it is incomplete and the API is subject to
change.

Please drop me a line if you would like to help with this effort or
have suggestions for how the API should shape up.

## Usage

    (use 'ensembl.core)
 
    (def ensreg (registry :ensembldb))

    (def human (species "human" :registry ensreg)

or

    (with-registry ensreg
      (def human (species "human")))

    (list-chromosomes human)

## License

Copyright (C) 2012 Ray Miller <ray@1729.org.uk>.

Distributed under the Eclipse Public License, the same as Clojure.
