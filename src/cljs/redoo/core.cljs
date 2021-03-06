(ns redoo.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame :refer [dispatch-sync dispatch]]
              [re-frisk.core :refer [enable-re-frisk!]]
              [redoo.events]
              [redoo.subs]
              [redoo.routes :as routes]
              [redoo.views :as views]
              [redoo.config :as config]
              [redoo.db :as db]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (enable-re-frisk!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (routes/app-routes)
  (dispatch-sync [:initialise-db])
  ;; Fixme : only run when db has no items and debug is true
  (run! #(dispatch-sync [:add-todo %]) db/fixtures)
  (dev-setup)
  (mount-root))

