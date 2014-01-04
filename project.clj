(defproject com.hypirion/sc-sugar "0.1.0-SNAPSHOT"
  :description "(Temporary) sugar macros for simple-check."
  :url "https://github.com/hyPiRion/sc-sugar"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [reiddraper/simple-check "0.5.3"]]
  :profiles {:dev {:plugins [[codox "0.6.4"]]
                   :codox {:output-dir "codox"}}}
  :deploy-branches ["stable"])
