(ns num-text.num-text-spec
  (:require [clojure.spec :as s]
            [num-text.core :refer :all]))


; input num

; output text

; intermediary format as a vector of keywords

; Not interesting types but there are interesting constraints

(s/conform pos? 0)

(map #(s/conform pos? %) (range 5))

(s/valid? pos? 0)

(map #(s/valid? pos? %) (range 5))

(s/def :num/number pos?)

(s/valid? :num/number 0)

(map #(s/valid? :num/number %) (range 5))


; for a sample of known correct values 1 - 19
(s/def :num/known-valid-single
  (s/and #(get inverse-singles %) keyword?))

(map #(s/valid? :num/known-valid-single %)
     (map #(first (num-representation %)) (range 1 20)))

; for 20, 30 ... 90
(s/def :num/known-valid-x10
  (s/and #(get inverse-tens %) keyword?))

(map #(s/valid? :num/known-valid-x10 %)
     (map #(first (num-representation %)) (range 20 91 10)))


; all answers 100 to 1000 have 4 or 5 words
(s/def :num/good-wc-100-to-1k
  (s/or :words-4 #(= 4 (count %))
        :words-5 #(= 5 (count %))))

(map #(s/valid? :num/good-wc-100-to-1k %)
     (map #(num-representation %) (random-sample 0.005 (range 100 1000))))

; odd numbers between 121 and 200 are five words
(s/def :num/good-wc-121-200 #(= 5 (count %)))

(distinct
  (map #(s/valid? :num/good-wc-121-200 %)
       (map #(num-representation %)
            (filter odd? (random-sample 0.5 (range 121 200))))))