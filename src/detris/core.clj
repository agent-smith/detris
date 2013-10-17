(ns detris.core
  (:gen-class)
  (:require [clojure.java.io :as io]))

(def BOARD-WIDTH 10)
(def MAX-BOARD-HEIGHT 100)

(def BOARD [[0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]])

(def Q [[1 1]
        [1 1]])

(def L [[1 0]
        [1 0]
        [1 1]])

(def J [[0 1]
        [0 1]
        [1 1]])

(def S [[0 1 1]
        [1 1 0]])

(def Z [[1 1 0]
        [0 1 1]])

(def T [[1 1 1]
        [0 1 0]])

(def I [[1 1 1 1]])

(defn convert-to-letter [letter-str]
  (case letter-str
    "Q" Q
    "L" L
    "J" J
    "S" S
    "Z" Z
    "T" T
    "I" I))

(defn new-vec [n] (into [] (repeat n 0)))
(def new-row (new-vec BOARD-WIDTH))

(defn width [x] (count (first x)))
(defn height [x] (count x))

(defn sum-row [row] (reduce + row))

(defn row-full? [row] (= BOARD-WIDTH (sum-row row)))
(defn row-empty? [row] (= 0 (sum-row row)))

(defn pad [v length offset]
  (let [front (new-vec offset)
        back (new-vec (- length (+ offset (count v))))]
    (into [] (concat front v back))))

(defn pad-letter [letter offset]
  (loop [padded-letter letter
         letter-row 0]
    (cond (> letter-row (- (count letter) 1)) padded-letter
          :else (recur (assoc padded-letter letter-row (pad (get padded-letter letter-row) BOARD-WIDTH offset))
                       (inc letter-row)))))

(defn add-letter-to-top [board letter offset]
  (into [] (concat (pad-letter letter offset) board)))

(defn zero-rows [n]
  (into [] (for [x (range 0 n)] (vec new-row))))

(defn zero-out-letter [letter]
  (zero-rows (count letter)))

(defn extract-rows [board start end]
  (subvec board start (inc end)))

(defn merge-rows [row1 row2]
  (loop [result row1
         i 0]
    (cond (= i (count row1)) result
          :else
            (let [col-sum (+ (get row1 i) (get row2 i))]
              (recur (assoc result i (if (> col-sum 0) 1 0))
                     (inc i))))))

(defn merge-n-rows [rows1 rows2]
  (loop [result rows1
         acc 0]
    (cond (= acc (count rows1)) result
          :else (recur (assoc result acc (merge-rows (get rows1 acc) (get rows2 acc)))
                       (inc acc)))))

(defn row-conflict? [row1 row2]
  (loop [row1 row1
         row2 row2]
    (cond (empty? row1) false
      (> (+ (first row1) (first row2)) 1) true
      :else (recur (rest row1) (rest row2)))))

(defn letter-level [board letter offset]
  (let [letter-rows (pad-letter letter offset)]
    (loop [acc (count letter-rows)]
      (cond (= acc (count board)) (dec acc)
            (row-conflict? (get board acc) (last letter-rows)) (dec acc)
            (and (> (count letter-rows) 1)
                 (row-conflict? (get board acc) (get letter-rows (- (count letter-rows) 2)))) acc
            :else (recur (inc acc))))))

(defn replace-rows [board begin rows]
  (loop [mod-board board
         r-acc 0
         b-acc begin]
    (cond (= r-acc (count rows)) mod-board
          :else (recur (assoc mod-board b-acc (get rows r-acc))
                       (inc r-acc)
                       (inc b-acc)))))

(defn remove-full-rows [board]
  (remove row-full? board))

(defn remove-empty-rows [board]
  (into [] (remove row-empty? board)))

(defn zero-offset-cols [row offset set-last]
  (reduce (fn [acc [idx row]]
            (assoc acc idx row))
          row
          (if set-last
            {offset 0, (inc offset) 0}
            {offset 0})))


(defn zero-offset-letter [rows letter offset]
  (cond (or (= L letter) (= J letter) (= S letter))
          (into [] (for [x rows] (zero-offset-cols x offset true)))
        (or (= Z letter))
          (into [] (for [x rows] (zero-offset-cols x (inc offset) true)))
        (= T letter)
          (into [] (for [x rows] (zero-offset-cols x (inc offset) false)))
        :else
          rows))

; one day I will prob laugh at this fn and refator it effortlessly
(defn place-on-board [board letter offset]
  (let [padded-board (add-letter-to-top board letter offset)]
    (let [end (letter-level padded-board letter offset)]
        (let [begin (- end (dec (count letter)))
              letter-rows (extract-rows padded-board 0 (dec (count letter)))
              board-rows (zero-offset-letter (extract-rows padded-board begin end) letter offset)]
          (let [merged-rows (merge-n-rows letter-rows board-rows)
                padded-letter (pad-letter letter offset)]
            (let [replaced-rows (replace-rows padded-board begin merged-rows)
                  letter-end (dec (count letter))]
               (remove-full-rows
                 (remove-empty-rows
                   (cond (= end letter-end) replaced-rows
                         (> (- end letter-end) letter-end) (replace-rows replaced-rows 0 (zero-out-letter letter))
                         :else (replace-rows replaced-rows 0 (zero-rows (- end letter-end))))))))))))

(defn write-ans [file-path ans]
  (with-open [wtr (io/writer file-path :append true)]
    (.write wtr (str ans "\n"))))

(defn calc-row-height [line write-to-file]
      (loop [board BOARD
             letter-nums (clojure.string/split line #",")]

        (cond (and (empty? letter-nums) write-to-file)
                (write-ans "output.txt" (height board))
              (empty? letter-nums)
                (height board)
              :else
                (do
                  (let [letter (convert-to-letter (str (first (first letter-nums))))
                        offset (read-string (str (get (first letter-nums) 1)))]
                    (recur (place-on-board board letter offset)
                           (rest letter-nums)))))))

(defn play-game [fname]
  (with-open [reader (io/reader fname)]
    (doseq [line (line-seq reader)]
      (calc-row-height line true))))

(defn -main
  [& args]
    (play-game "input.txt"))
