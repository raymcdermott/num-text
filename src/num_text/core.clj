(ns num-text.core)

(defn num->text
  [num]
  (let [low-nums  {1  "one" 2 "two" 3 "three" 4 "four" 5 "five" 6 "six"
                   7  "seven" 8 "eight" 9 "nine" 10 "ten" 11 "eleven"
                   12 "twelve" 13 "thirteen" 14 "fourteen" 15 "fifteen"
                   16 "sixteen" 17 "seventeen" 18 "eighteen" 19 "nineteen"}

        tens      {20 "twenty" 30 "thirty" 40 "forty" 50 "fifty" 60 "sixty"
                   70 "seventy" 80 "eighty" 90 "ninety"}

        ten-1000s {1 "ten" 2 "twenty" 3 "thirty" 4 "forty" 5 "fifty"
                   6 "sixty" 7 "seventy" 8 "eighty" 9 "ninety"}]

    (defn teenables [n]
      "Generate a list of n numbers divisible by 10 000"
      (reduce (fn [a b] (conj a (*' (last a) 1000)))
              [10000] (range n)))

    (defn teen?
      [num]
      "Should we express this number using the first one or two digits?
       19191 919 - should be 19 million, remainder 191 919
       2919 191 - should be 2 million, remainder 919 191
       391919 - should be 3 hundred, remainder 91 919
              - three hundred and ninety one thousand nine hundred and nineteen
       49191 - should be 49 thousand, remainder 191
             - forty nine thousand one hundred and ninety one
       5919 - should be 5 thousand remainder 919
            - five thousand nine hundred and nineteen
       619 - should be 6 hundred remainder 19
           - six hundred and nineteen
       79 - should be 79
          - seventy nine
       8 - should be 8
         - eight"

      (if (and (>= num 10) (< num 100))
        num
        (let [teenish (teenables 5)
              two-digits (remove nil?
                                 (map (fn [lower-bound]
                                        (let [upper-bound (* lower-bound 10)
                                              divisor     (/ lower-bound 10)]
                                          (if (and (>= num lower-bound) (<= num upper-bound))
                                            (let [carry (mod num divisor)
                                                  teen  (/ (- num carry) divisor)]
                                              [teen carry])))) teenish))]
          (if (not-empty two-digits)
            two-digits
            ["TODO" num]))))

    (defn iter
      ([n divisor text]
       (iter n divisor text false))

      ([n divisor text tens?]
       (let [unit  (quot n divisor)
             carry (- n (* divisor unit))]
         (println "carry" carry)
         (str (cond
                (and tens? (< unit 20)) (str (get ten-1000s unit) " ")
                (< unit 20) (str (get low-nums unit) text)
                :else (str (num->text carry) text))
              (num->text carry)))))

    (cond
      (< num 20)
      (str (get low-nums num))

      (and (>= num 20) (< num 100))
      (let [modulus (mod num 10)
            tenny   (- num modulus)]
        (if (zero? modulus)
          (str (get tens tenny))
          (str (get tens tenny) " "
               (num->text modulus))))

      (and (>= num 100) (<= num 1000))
      (iter num 100 " hundred ")

      (and (>= num 1000) (<= num 10000))
      (iter num 1000 " thousand ")

      (and (>= num 10000) (<= num 100000))
      (iter num 10000 " thousand " true)

      (and (>= num 100000) (<= num 1000000))
      (iter num 100000 " hundred ")

      (and (>= num 1000000) (<= num 10000000))
      (iter num 1000000 " million "))))

(let [nums (random-sample 0.5 (range 1 10))]
  (map (fn [n] [n (num->text n)]) nums))

(let [nums (random-sample 0.05 (range 10 100))]
  (map (fn [n] [n (num->text n)]) nums))

(let [nums (random-sample 0.005 (range 100 1000))]
  (map (fn [n] [n (num->text n)]) nums))

(let [nums (random-sample 0.0005 (range 1000 10000))]
  (map (fn [n] [n (num->text n)]) nums))

(let [nums (random-sample 0.00005 (range 10000 100000))]
  (map (fn [n] [n (num->text n)]) nums))



