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
    (is (= 20 (apply + (map #(count (num-representation %))
                            (range 1 21))))))

  (testing "results of 10, 20, 30 ... 90 are a single word"
    (is (= 9 (apply + (map #(count (num-representation %))
                           (range 10 99 10))))))

  (testing "random odd numbers between 20 100 are two words"
    (let [sample (filter odd? (random-sample 0.5 (range 20 100)))]
      (is (= (* 2 (count sample))
             (apply + (map #(count (num-representation %)) sample))))))

  (testing "random even numbers (not a multiple of 10) between 20 100 are two words"
    (let [sample (filter #(and (not (= 0 (mod % 10)))
                               (even? %))
                         (random-sample 0.5 (range 20 100)))]
      (is (= (* 2 (count sample))
             (apply + (map #(count (num-representation %)) sample))))))

  (testing "numbers between 101 and 120 are four words"
    (let [sample (range 101 121)]
      (is (= (* 4 (count sample))
             (apply + (map #(count (num-representation %)) sample))))))

  (testing "random odd numbers between 121 and 200 are five words"
    (let [sample (filter odd? (random-sample 0.1 (range 121 200)))]
      (is (= (* 5 (count sample))
             (apply + (map #(count (num-representation %)) sample))))))

  (testing "random odd numbers between 921 and 1000 are five words"
    (let [sample (filter odd? (random-sample 0.1 (range 921 1000)))]
      (is (= (* 5 (count sample))
             (apply + (map #(count (num-representation %)) sample))))))

  (testing "first nn1, across all large units, ends in :one"
    (let [sample (map #(+ 1N %) unitable)]
      (is (= (count sample)
             (count (filter #(= :one %)
                            (map #(last (num-representation %)) sample)))))))

  (testing "last nn99, across all large units, ends in :nine"
    (let [sample (map #(- % 1N) unitable)]
      (is (= (count sample)
             (count (filter #(= :nine %)
                            (map #(last (num-representation %)) sample)))))))

  (testing "ten of all large units, starts with :ten and is two words"
    (let [sample (units 10000N 1000N (dec (count large-numbers-text)))]
      (is (= (count sample)
             (count (filter #(= :ten %)
                            (map #(first (num-representation %)) sample)))))
      (is (= (* 2 (count sample))
             (apply + (map #(count (num-representation %)) sample))))))

  (testing "first hundred of all large units, starts with :one :hundred and is three words"
    (let [sample (units 100000N 1000N (dec (count large-numbers-text)))]
      (is (= (count sample)
             (count (filter #(= [:one :hundred] %)
                            (map #(take 2 (num-representation %)) sample)))))
      (is (= (* 3 (count sample))
             (apply + (map #(count (num-representation %)) sample)))))))





; TODO use spec to define and constrain the solution

