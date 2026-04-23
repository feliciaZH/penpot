;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.ds.layout.page-header
  (:require-macros
   [app.main.style :as stl])
  (:require
   [app.main.ui.ds.foundations.typography.heading :refer [heading*]]
   [app.main.ui.ds.foundations.typography.text :refer [text*]]
   [rumext.v2 :as mf]))

(def ^:private schema:page-header
  [:map
   [:class {:optional true} :string]
   [:title :string]
   [:description {:optional true} :string]
   [:breadcrumbs {:optional true} :any]
   [:metadata {:optional true} :any]
   [:actions {:optional true} :any]])

(mf/defc page-header*
  {::mf/schema schema:page-header}
  [{:keys [class title description breadcrumbs metadata actions] :rest props}]
  (let [props (mf/spread-props props {:class [class (stl/css :header)]})]
    [:> :div props
     [:div {:class (stl/css :content)}
      (when breadcrumbs
        [:div {:class (stl/css :breadcrumbs)} breadcrumbs])
      [:> heading* {:level 2} title]
      (when description
        [:> text* {:typography "body-small"} description])
      (when metadata
        [:div {:class (stl/css :metadata)} metadata])]
     (when actions
       [:div {:class (stl/css :actions)} actions])]))
