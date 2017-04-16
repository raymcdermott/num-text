(ns num-text.core)

(def x1 [:one :two :three :four :five :six :seven :eight :nine
         :ten :eleven :twelve :thirteen :fourteen :fifteen :sixteen :seventeen :eighteen :nineteen])
(def singles (zipmap (range 1 20) x1))

(def x10 [:twenty :thirty :forty :fifty :sixty :seventy :eighty :ninety])
(def tens (zipmap (range 20 99 10) x10))

(defn nums-lt-100
  [num]
  {:pre [(pos? num) (< num 100)]}
  (if-let [answer (or (get singles num)
                      (get tens num))]
    [answer]
    (let [ten-part  (quot num 10)
          ten-whole (* 10 ten-part)]
      [(get tens ten-whole) (nums-lt-100 (- num ten-whole))])))

(defn inject-and
  [num-vec]
  (let [size (count num-vec)]
    (cond (<= size 2) num-vec
          :else (concat (take 2 num-vec) [:and] (drop 2 num-vec)))))

(defn nums-gt-100-lt-1000
  [num]
  {:pre [(>= num 100) (< num 1000)]}
  (let [hundreds  (quot num 100)
        remainder (- num (* hundreds 100))
        result    [(get singles hundreds) :hundred
                   (if-not (zero? remainder)
                     (nums-lt-100 remainder))]]
    (inject-and (remove nil? result))))

(defn units [start step count]
  "Generate a list of numbers of count length start
  starting at start multiplied by step"
  (reduce (fn [a b] (conj a (* (last a) step)))
          [start] (range count)))

(def large-numbers-text [:thousand :million :billion :trillion
                         :quadrillion :quintillion :sextillion
                         :septillion :octillion :nonillion
                         :decillion :undecillion :duodecillion
                         :tredecillion :quattuordecillion])

(def unitable (units 1000N 1000N (dec (count large-numbers-text))))
(def unit-boundaries (map (fn [u] [u (* u 1000N)]) unitable))

(def large-number-map (zipmap unitable large-numbers-text))
(def inverse-large-numbers-map
  (reduce merge {} (map (juxt val key) large-number-map)))

(defn which-bounds? [num unit-bounds]
  (remove nil? (map (fn [[lower upper]]
                      (if (and (>= num lower)
                               (< num upper))
                        lower)) unit-bounds)))

(defn which-unit? [num]
  (let [unit (which-bounds? num unit-boundaries)]
    (get large-number-map (first unit))))

(defn num-representation
  [num]
  (flatten
    (cond
      (< num 100) (nums-lt-100 num)
      (< num 1000) (nums-gt-100-lt-1000 num)
      :else (let [unit       (which-unit? num)
                  divisor    (unit inverse-large-numbers-map)
                  first-part (quot num divisor)
                  remainder  (- num (* divisor first-part))]
              (if (zero? remainder)
                [(num-representation first-part) unit]
                [(num-representation first-part)
                 unit
                 (num-representation remainder)])))))

(defn num-vec->text
  [num-vec]
  (clojure.string/capitalize
    (apply str (interpose " " (map name num-vec)))))

(defn num->text
  [num]
  (num-vec->text (num-representation num)))

(num->text 123456789)