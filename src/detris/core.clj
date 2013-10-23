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

(defn
  pad [v length offset]
  "For a given vector, of length offset, put zeros in front and back (i.e. pad)
  of the vector so that it's new length is equal to the given length."

  (let [front (new-vec offset)
        back  (new-vec (- length (+ offset (count v))))]
    (into [] (concat front v back))))

(defn
  pad-letter [letter offset]
  "For a given letter, pad it with zeros (in front and back) so that it is positioned,
  within a new vector, starting at the offset (0..length-1)."

  (loop [padded-letter letter
         letter-row 0]
    (cond (> letter-row (- (height letter) 1)) padded-letter
          :else (recur (assoc padded-letter letter-row (pad (get padded-letter letter-row) BOARD-WIDTH offset))
                       (inc letter-row)))))

(defn
  add-letter-to-top [board letter offset]
  "Adds a padded letter to the top of a board, at the specified offset."
  (into [] (concat (pad-letter letter offset) board)))

(defn zero-rows [n]
  "Create N new-row vectors."
  (into [] (for [x (range 0 n)] (vec new-row))))

(defn zero-out-letter [letter]
  (zero-rows (count letter)))

(defn extract-rows [board start end]
  "Select out vector rows, from a board, in the range of start (inclusive) and end (inclusive)."
  (subvec board start (inc end)))

(defn merge-rows [row1 row2]
  "Given 2 rows of the same length, which contain 1s or 0s, colapse them into one row.
  The resulting row will also contain only 1s or 0s, where a 1 represents the merging
  of either a 1 & 0 or 1 & 1, and a 0 represents the merging of only a 0 & 0.  Thus,
  merging [1 1 0 0] and [1 0 1 0] would result in [1 1 1 0]."
  (into [] (map #(if (>= (+ %1 %2) 1) 1 0) row1 row2)))

(defn merge-n-rows [rows1 rows2]
  "Applies the rules of merge-rows for each pair of vectors in N vectors."

  (loop [result rows1
         acc 0]
    (cond (= acc (count rows1)) result
          :else (recur (assoc result acc (merge-rows (get rows1 acc) (get rows2 acc)))
                       (inc acc)))))

(defn row-conflict? [row1 row2]
  "Given 2 vectors, each containing only 1s or 0s, determines if adding the elements at
  the same index in each vector will result in a number greater than 1.  If so, then it's
  considered a conflict, and returns true.  Otherwise, return false."

  (loop [row1 row1
         row2 row2]
    (cond (empty? row1) false
      (> (+ (first row1) (first row2)) 1) true
      :else (recur (rest row1) (rest row2)))))

(defn letter-level [board letter offset]
  "Given a board, with a padded letter added to the top (this is a requirement!), find
  where the letter should be placed when gravity pulls the letter down toward the board.
  The number returned represents the row on the board where the bottom most piece of the
  letter will be placed on the board.  Note that this is not necessarily the bottom-most
  piece of the letter, since the letter could come to rest on the board, where it's
  'hooked' on with it's top-post piece, but it's bottom-most piece is dangling with empty
  board space underneath (e.g. S/Z/T can do this when their upper left/right pieces get
  hooked onto the board)."

  (let [letter-rows (pad-letter letter offset)]
    (loop [acc (count letter-rows)]
      (cond (= acc (count board)) (dec acc)
            (row-conflict? (get board acc) (last letter-rows)) (dec acc)
            (and (> (count letter-rows) 1)
                 (row-conflict? (get board acc) (get letter-rows (- (count letter-rows) 2)))) acc
            :else (recur (inc acc))))))

(defn replace-rows [board begin rows]
  "Uses the given vector rows to replace existing vector rows on a board, starting at
  the begin index, and ending at the begin index + height of the rows.  E.g. if the
  board has 4 rows, and the rows passed have 2 vectors, then for begin=1, the rows
  replaced are between 1 (inclusive) and 2 (inclusive)."

  (loop [mod-board board
         r-acc 0
         b-acc begin]
    (cond (= r-acc (count rows)) mod-board
          :else (recur (assoc mod-board b-acc (get rows r-acc))
                       (inc r-acc)
                       (inc b-acc)))))

(defn remove-full-rows [board]
  "Given a board, take out all rows that have all 1s."
  (remove row-full? board))

(defn remove-empty-rows [board]
  "Given a board, take out all rows that have all 0s."
  (into [] (remove row-empty? board)))


(defn zero-offset-cols [row col set-next-col]
  "Given a vector, set the value at col (and optionally col+1) to 0."

  (reduce (fn [acc [idx row]]
            (assoc acc idx row))
          row
          (if set-next-col
            {col 0, (inc col) 0}
            {col 0})))


(defn zero-offset-letter [rows letter offset]
  "Given a vector of vectors (i.e. rows that contain a letter at some offset),
  use zero-offset-cols to replace the 1s with 0s for all pieces of the letter
  that would fall toward the board.
  Note: Sometimes, when a non-solid letter (L/J/S/Z/T) is moved down, it will
  have some of it's residual self left over on the board.  So, we want to
  zero out the places in its solid-part (e.g. this is offset and offset+1 for S,
  and offset+1 and offset+2 for Z)."

  (cond (or (= L letter) (= J letter) (= S letter))
          (into [] (for [x rows] (zero-offset-cols x offset true)))
        (or (= Z letter))
          (into [] (for [x rows] (zero-offset-cols x (inc offset) true)))
        (= T letter)
          (into [] (for [x rows] (zero-offset-cols x (inc offset) false)))
        :else
          rows))

(defn place-on-board [board letter offset]
  (let [padded-board  (add-letter-to-top board letter offset)
        letter-end    (dec (height letter))
        end           (letter-level padded-board letter offset)
        begin         (- end letter-end)
        letter-rows   (extract-rows padded-board 0 letter-end)
        board-rows    (zero-offset-letter (extract-rows padded-board begin end) letter offset)
        merged-rows   (merge-n-rows letter-rows board-rows)
        padded-letter (pad-letter letter offset)
        replaced-rows (replace-rows padded-board begin merged-rows)]
    "Given a board and a letter, do the following:
    1) Place the padded letter, at the given offset, at the top of the board.
    2) Find the place where the letter will rest on the board at the given offset.
    3) Replace the rows on the board where the letter will come to rest.
    4) Remove any empty rows on the board.
    5) Remove any full fows on the board."

    (remove-full-rows
      (remove-empty-rows
        (cond (= end letter-end) replaced-rows
              (> (- end letter-end) letter-end) (replace-rows replaced-rows 0 (zero-out-letter letter))
              :else (replace-rows replaced-rows 0 (zero-rows (- end letter-end))))))))

(defn write-ans [file-path ans]
  (with-open [wtr (io/writer file-path :append true)]
    (.write wtr (str ans "\n"))))

(defn calc-row-height [line write-to-file]
  "For all letter coordinates in the given line, place the letters on the board
  and calculate the resulting board height.  Then, if write-to-file=true, append
  the board hieght on new line in the given file."

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

(def DEFAULT-INPUT-FILE "input.txt")
(def DEFAULT-OUTPUT-FILE "output.txt")

(defn play-game [fname]
  (io/delete-file DEFAULT-OUTPUT-FILE true)
  (with-open [reader (io/reader fname)]
    (doseq [line (line-seq reader)]
      (calc-row-height line true))))

(defn -main
  [& args]
    (play-game (if args (first args) DEFAULT-INPUT-FILE)))
