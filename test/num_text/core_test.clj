(ns num-text.core-test
  (:require [clojure.test :refer :all]
            [num-text.core :refer :all]))

(deftest external-layer
  (testing "for a sample of known correct values"
    (is (and
          (= "One" (num->text 1))
          (= "Ten" (num->text 10))
          (= "Nineteen" (num->text 19))
          (= "Eighty five"
             (num->text 85))
          (= "Eight hundred and fifty nine"
             (num->text 859))
          (= "Seven thousand five hundred and eighty nine"
             (num->text 7589))
          (= "Sixty five thousand seven hundred and eighty nine"
             (num->text 65789))
          (= "Seven hundred and fifty six thousand seven hundred and eighty nine"
             (num->text 756789))
          (= "Four million two hundred and fifty six thousand seven hundred and eighty nine"
             (num->text 4256789))
          (= "Thirty two million four hundred and fifty six thousand seven hundred and eighty nine"
             (num->text 32456789))
          (= "One hundred and twenty three million four hundred and fifty six thousand seven hundred and eighty nine"
             (num->text 123456789))))))

; this should never happen
(defn adjacent-ands?
  [num-vec]
  (not (empty?
         (filter #(> (count %) 1)
                 (filter #(= :and (first %))
                         (partition-by #(= :and %) num-vec))))))

(defn matches-word-count?
  [target-count sample-numbers]
  (= 1 (count (distinct
                (filter #(= target-count %)
                        (map #(count (num-representation %))
                             sample-numbers))))))

(defn matches-words?
  [predicate map-fn sample-numbers]
  (= 1 (count (distinct (filter #(= predicate %)
                                (map map-fn sample-numbers))))))

(deftest internal-layer

  (testing "for correct internal mapping numbers 1 - 19 inclusive"
    (let [result (map #(get singles %) (range 1 20))]
      (is (= (count result) (count (filter keyword? result))))
      (is (= 0 (count (filter nil? result))))))

  (testing "for a sample of known correct values 1 - 19"
    (is (and (= :one (first (num-representation 1)))
             (= :ten (first (num-representation 10)))
             (= :nineteen (first (num-representation 19))))))

  (testing "boundaries on the internal mapping of 1 - 19"
    (is (and (= nil (singles 20))
             (= nil (singles 0)))))

  (testing "zero is rejected"
    (is (thrown? AssertionError (num-representation 0))))

  (testing "results up to 20 are a single word"
    (is (= 20 (apply + (map #(count (num-representation %)) (range 1 21))))))

  (testing "results of 10, 20, 30 ... 90 are a single word"
    (is (matches-word-count? 1 (range 10 99 10))))

  (testing "odd numbers between 20 100 are two words"
    (let [sample (filter odd? (range 20 100))]
      (is (= (matches-word-count? 2 sample)))))

  (testing "even numbers (not a multiple of 10) between 20 100 are two words"
    (let [sample (filter #(and (not (= 0 (mod % 10))) (even? %)) (range 20 100))]
      (is (matches-word-count? 2 sample))))

  (testing "numbers between 101 and 120 are four words"
    (is (matches-word-count? 4 (range 101 121))))

  (testing "odd numbers between 121 and 200 are five words"
    (let [sample (filter odd? (range 121 200))]
      (is (matches-word-count? 5 sample))
      (is (empty? (filter true? (map #(adjacent-ands? (num-representation %)) sample))))))

  (testing "odd numbers between 921 and 1000 are five words"
    (let [sample (filter odd? (range 921 1000))]
      (is (matches-word-count? 5 sample))
      (is (empty? (filter true? (map #(adjacent-ands? (num-representation %)) sample))))))

  (testing "first nn1, across all large units, ends in :one and is 4 words"
    (let [sample (map #(+ 1N %) unitable)]
      (is (matches-words? :one #(last (num-representation %)) sample))
      (is (matches-word-count? 4 sample))
      (is (empty? (filter true? (map #(adjacent-ands? (num-representation %)) sample))))))

  (testing "first nn101, across all large units, ends in :one"
    (let [sample (map #(+ 101N %) unitable)]
      (is (matches-words? :one #(last (num-representation %)) sample))
      (is (empty? (filter true? (map #(adjacent-ands? (num-representation %)) sample))))))

  (testing "last nn99, across all large units, ends in :nine"
    (let [sample (map #(- % 1N) unitable)]
      (is (matches-words? :nine #(first (num-representation %)) sample))
      (is (empty? (filter true? (map #(adjacent-ands? (num-representation %)) sample))))))

  (testing "ten of all large units, starts with :ten and is two words"
    (let [sample (units 10000N 1000N (dec (count large-numbers-text)))]
      (is (matches-words? :ten #(first (num-representation %)) sample))
      (is (matches-word-count? 2 sample))
      (is (empty? (filter true? (map #(adjacent-ands? (num-representation %)) sample))))))

  (testing "first hundred of all large units, starts with :one :hundred and is 3 words"
    (let [sample (units 100000N 1000N (dec (count large-numbers-text)))]
      (is (matches-words? [:one :hundred] #(take 2 (num-representation %)) sample))
      (is (matches-word-count? 3 sample))
      (is (empty? (filter true? (map #(adjacent-ands? (num-representation %)) sample)))))))



; TODO use spec to define and constrain the solution

