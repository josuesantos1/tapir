(ns tapir.handlers.storage
  (:require [clojure.java.io :as io]

            [clucie.core :as core]
            [clucie.analysis :as analysis]
            [clucie.store :as store]))

;; {:key "" :value [] :_ttl "" :_created "" :_modified ""}


(def analyzer ^:private (analysis/standard-analyzer))
#_(def index-store ^:private (store/memory-store))

(def ^:private index (store/disk-store "storage/tapir"))

;; memory store
(comment (defn insert-in-memory
           [store record]
           ())

         (defn get-in-memory
           [store record]
           ())

         (defn- update-in-memory
           [store record]
           ())

         (defn- delete-in-memory
           [store record]
           ()))

;; disk store
(defn insert-record
  [record]
  (core/add! index
             record
             [:key :value :_ttl :_created :_modified]
             analyzer))

(defn select-record
  [record & args]
  (let [limit (:limit args)
        page (:page args)
        max-page (:max-page args)]
    (core/search index
                 record
                 (or 10 max-page)
                 analyzer
                 (or 0 page)
                 (or 100 limit))))

#_(defn update-record
  [store record where]
  (let [index (index-store (name store))]
    (core/update! index
                  record
                  [:store]
                  :key where
                  analyzer)))

(defn delete-record
  [record]
  (core/delete! index :key record analyzer))


