(ns example
  {:clj-kondo/config '{:lint-as {promesa.core/let clojure.core/let
                                 example/defp clojure.core/def}}}
  (:require
   ["puppeteer$default" :as puppeteer]
   ["fs/promises" :as fs]
   [clojure.test :as t :refer [deftest is async]]
   [promesa.core :as p]
   [squint.compiler :as sc]))

(comment
  (sc/compile-string (str '#(* 2 %))))


(def URL "https://www.wikipedia.org")

(def SEARCH_SELECTOR "#searchInput")

(def SEARCH_QUERY "clojure")

(def SEARCH_BUTTON "[type='submit']")

(def PRODUCTS_LIST_CONTAINER ".category-list-body")

(defn- timeout [ms]
  (js/Promise. (fn [r] (js/setTimeout r ms))))

(defn pupp []
  (p/let [browser (.launch puppeteer #js {:headless false})
          page (.newPage browser)
          target (.target page)

          _ (.goto page URL)
          inp (.waitForSelector page SEARCH_SELECTOR)
          _ (.type page SEARCH_SELECTOR SEARCH_QUERY)

          _ (.all js/Promise #js [(.click page SEARCH_BUTTON)
                                  (.waitForNavigation page)])

          _ (timeout 3000)
          ev (.$$eval page "div" (js/eval "divs => divs.length"))
          _ (println ev)]))

(pupp)