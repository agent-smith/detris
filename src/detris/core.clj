(ns detris.core
  (:gen-class)
  (:require [clojure.java.io :as io]))

(def BOARD [])

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
(def new-row (new-vec 10))

(defn width [x] (count (first x)))
(defn height [x] (count x))

(defn sum-row [row] (reduce + row))

(defn row-full? [row] (= 10 (sum-row row)))

(defn add-to-top [board row]
  (into [] (concat [row] board)))

(defn replace-in-row [row replacee offset]
  (into [] (concat (subvec row 0 offset) replacee (subvec row (+ offset (count replacee))))))

(defn pad [v length offset]
  (let [front (new-vec offset)
        back (new-vec (- length (+ offset (count v))))]
    (into [] (concat front v back))))

(defn remove-last [v]
  (if (or (empty? v) (= 1 (count v))) []
      (subvec v 0 (- (count v) 1))))

(defn add-letter-to-top [board letter offset]
    (loop [mod-board board
           partial-letter letter]
      (cond (empty? partial-letter) mod-board
            :else (recur (add-to-top mod-board (pad (last partial-letter) 10 offset))
                         (remove-last partial-letter)))))

(defn row-conflict? [row1 row2]
  (loop [row1 row1
         row2 row2]
    (cond (empty? row1) false
      (> (+ (first row1) (first row2)) 1) true
      :else (recur (rest row1) (rest row2)))))

(defn place-on-board [board letter offset row]
    (cond (empty? letter)
            board

          (or (> row (count board)) (= row (count board)))
            (place-on-board (add-letter-to-top board letter offset)
                            (remove-last letter)
                            offset
                            (inc row))

          (and (= 0 row) (row-conflict? (pad (last letter) 10 offset) (get board row)))
            (add-letter-to-top board letter offset)

          (row-conflict? (pad (last letter) 10 offset) (get board row))
            (place-on-board (assoc board
                                   (dec row)
                                   (replace-in-row (get board (dec row)) (last letter) offset))
                            (remove-last letter)
                            offset
                            (dec row))

          :else
            (place-on-board board
                            letter
                            offset
                            (inc row))))

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
                    (recur (place-on-board board letter offset 0)
                           (rest letter-nums)))))))

(defn play-game [fname]
  (with-open [reader (io/reader fname)]
    (doseq [line (line-seq reader)]
      (calc-row-height line true))))

(defn -main
  [& args]
    (play-game "input.txt"))
