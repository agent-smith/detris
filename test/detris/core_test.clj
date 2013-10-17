(ns detris.core-test
  (:require [clojure.test :refer :all]
            [detris.core :refer :all]))

(deftest width-height-test
  (testing "Q should 2x2."
    (is (= 2 (width Q)))
    (is (= 2 (height Q))))

  (testing "L should be 2x3."
    (is (= 2 (width L)))
    (is (= 3 (height L))))

  (testing "J should be 2x3."
    (is (= 2 (width J)))
    (is (= 3 (height J))))

  (testing "S should 3x2."
    (is (= 3 (width S)))
    (is (= 2 (height S))))

  (testing "Z should be 3x2."
    (is (= 3 (width Z)))
    (is (= 2 (height Z))))

  (testing "T should be 3x2."
    (is (= 3 (width T)))
    (is (= 2 (height T))))

  (testing "I should be 4x1."
    (is (= 4 (width I)))
    (is (= 1 (height I))))
  )


(deftest row-full-test
  (testing "Row should be full."
    (is (row-full? [1 1 1 1 1 1 1 1 1 1])))

  (testing "Row should NOT be full."
    (not (row-full? [1 1 1 1 1 1 1 0 1 1])))

  (testing "Wrong size row should NOT be full."
    (not (row-full? [1 1 1 1 1])))
  )


(deftest pad-letter-test
  (testing "Pads Q (at offset 2) with zeros, expanding Q to the BOARD-WIDTH."
    (is (= [[0 0 1 1 0 0 0 0 0 0] [0 0 1 1 0 0 0 0 0 0]] (pad-letter Q 2))))

  (testing "Pads L (at offset 2) with zeros, expanding L to the BOARD-WIDTH."
    (is (= [[0 0 1 0 0 0 0 0 0 0] [0 0 1 0 0 0 0 0 0 0] [0 0 1 1 0 0 0 0 0 0]] (pad-letter L 2))))

  (testing "Pads J (at offset 2) with zeros, expanding J to the BOARD-WIDTH."
    (is (= [[0 0 0 1 0 0 0 0 0 0] [0 0 0 1 0 0 0 0 0 0] [0 0 1 1 0 0 0 0 0 0]] (pad-letter J 2))))

  (testing "Pads S (at offset 2) with zeros, expanding S to the BOARD-WIDTH."
    (is (= [[0 0 0 1 1 0 0 0 0 0] [0 0 1 1 0 0 0 0 0 0]] (pad-letter S 2))))

  (testing "Pads Z (at offset 2) with zeros, expanding Z to the BOARD-WIDTH."
    (is (= [[0 0 1 1 0 0 0 0 0 0] [0 0 0 1 1 0 0 0 0 0]] (pad-letter Z 2))))

  (testing "Pads T (at offset 2) with zeros, expanding T to the BOARD-WIDTH."
    (is (= [[0 0 1 1 1 0 0 0 0 0] [0 0 0 1 0 0 0 0 0 0]] (pad-letter T 2))))

  (testing "Pads I (at offset 2) with zeros, expanding I to the BOARD-WIDTH."
    (is (= [[0 0 1 1 1 1 0 0 0 0]] (pad-letter I 2))))
  )



(deftest zero-out-letter-test
  (testing "Creates N zero vectors, where N is the height of Q."
    (is (= [[0 0 0 0 0 0 0 0 0 0] [0 0 0 0 0 0 0 0 0 0]] (zero-out-letter Q))))

  (testing "Creates N zero vectors, where N is the height of L."
    (is (= [[0 0 0 0 0 0 0 0 0 0] [0 0 0 0 0 0 0 0 0 0] [0 0 0 0 0 0 0 0 0 0]] (zero-out-letter L))))

  (testing "Creates N zero vectors, where N is the height of J."
    (is (= [[0 0 0 0 0 0 0 0 0 0] [0 0 0 0 0 0 0 0 0 0] [0 0 0 0 0 0 0 0 0 0]] (zero-out-letter J))))

  (testing "Creates N zero vectors, where N is the height of S."
    (is (= [[0 0 0 0 0 0 0 0 0 0] [0 0 0 0 0 0 0 0 0 0]] (zero-out-letter S))))

  (testing "Creates N zero vectors, where N is the height of Z."
    (is (= [[0 0 0 0 0 0 0 0 0 0] [0 0 0 0 0 0 0 0 0 0]] (zero-out-letter Z))))

  (testing "Creates N zero vectors, where N is the height of T."
    (is (= [[0 0 0 0 0 0 0 0 0 0] [0 0 0 0 0 0 0 0 0 0]] (zero-out-letter T))))

  (testing "Creates N zero vectors, where N is the height of I."
    (is (= [[0 0 0 0 0 0 0 0 0 0]] (zero-out-letter I))))
  )



(deftest add-letter-to-top-test
  (testing "Adding Q to top of existing board."
    (is (= [[0 0 1 1 0 0 0 0 0 0]
            [0 0 1 1 0 0 0 0 0 0]
            [1 1 1 1 1 1 1 1 1 1]
            [1 1 1 1 1 1 1 1 1 1]]

           (add-letter-to-top
            [[1 1 1 1 1 1 1 1 1 1]
             [1 1 1 1 1 1 1 1 1 1]]
            Q 2))))

    (testing "Adding Z to top of existing board."
    (is (= [[0 0 1 1 0 0 0 0 0 0]
            [0 0 0 1 1 0 0 0 0 0]
            [1 1 1 1 1 1 1 1 1 1]
            [1 1 1 1 1 1 1 1 1 1]]

           (add-letter-to-top
            [[1 1 1 1 1 1 1 1 1 1]
             [1 1 1 1 1 1 1 1 1 1]]
            Z 2))))

    (testing "Adding S to top of existing board."
    (is (= [[0 0 0 1 1 0 0 0 0 0]
            [0 0 1 1 0 0 0 0 0 0]
            [1 1 1 1 1 1 1 1 1 1]
            [1 1 1 1 1 1 1 1 1 1]]

           (add-letter-to-top
            [[1 1 1 1 1 1 1 1 1 1]
             [1 1 1 1 1 1 1 1 1 1]]
            S 2))))

  (testing "Adding J to top of existing board."
    (is (= [[0 0 0 1 0 0 0 0 0 0]
            [0 0 0 1 0 0 0 0 0 0]
            [0 0 1 1 0 0 0 0 0 0]
            [1 1 1 1 1 1 1 1 1 1]
            [1 1 1 1 1 1 1 1 1 1]]

           (add-letter-to-top
            [[1 1 1 1 1 1 1 1 1 1]
             [1 1 1 1 1 1 1 1 1 1]]
            J 2))))

  (testing "Adding L to top of existing board."
    (is (= [[0 0 1 0 0 0 0 0 0 0]
            [0 0 1 0 0 0 0 0 0 0]
            [0 0 1 1 0 0 0 0 0 0]
            [1 1 1 1 1 1 1 1 1 1]
            [1 1 1 1 1 1 1 1 1 1]]

           (add-letter-to-top
            [[1 1 1 1 1 1 1 1 1 1]
             [1 1 1 1 1 1 1 1 1 1]]
            L 2))))

    (testing "Adding T to top of existing board."
    (is (= [[0 0 1 1 1 0 0 0 0 0]
            [0 0 0 1 0 0 0 0 0 0]
            [1 1 1 1 1 1 1 1 1 1]
            [1 1 1 1 1 1 1 1 1 1]]

           (add-letter-to-top
            [[1 1 1 1 1 1 1 1 1 1]
             [1 1 1 1 1 1 1 1 1 1]]
            T 2))))

    (testing "Adding I to top of existing board."
    (is (= [[0 0 1 1 1 1 0 0 0 0]
            [1 1 1 1 1 1 1 1 1 1]
            [1 1 1 1 1 1 1 1 1 1]]

           (add-letter-to-top
            [[1 1 1 1 1 1 1 1 1 1]
             [1 1 1 1 1 1 1 1 1 1]]
            I 2))))
  )


(deftest row-conflict-test
  (testing "True if 2 vectors have overlapping 1s, otherwise false."
    (is (= true (row-conflict? [0 0 0 0 1 1 0 0 0 0]
                               [0 0 0 0 0 1 1 0 0 0])))
    (is (= false (row-conflict? [0 0 0 0 0 0 0 0 0 0]
                                [1 1 0 0 0 0 0 0 0 0]))))
  )


(deftest place-on-board-test
  (testing "Adding letter to an existing board."
    (is (= [[1 1 1 1 0 1 1 0 1 1]
            [1 1 1 1 0 1 1 0 1 1]]

           (place-on-board
            [[1 1 0 0 0 1 1 0 1 1]
             [1 1 0 0 0 1 1 0 1 1]]
            Q 2)))))

(deftest place-on-board-test
  (testing "Adding letter to an existing board."
    (is (= [[0 0 1 1 0 0 0 0 0 0]
            [0 0 1 1 0 0 0 0 0 0]]

           (place-on-board
            [[0 0 0 0 0 0 0 0 0 0]
             [0 0 0 0 0 0 0 0 0 0]
             [0 0 0 0 0 0 0 0 0 0]
             [0 0 0 0 0 0 0 0 0 0]]
            Q 2)))))


(deftest extract-rows-test
  (testing "Adding letter to an existing board."
    (is (= [[1 1 1 1 1 1 1 1 1 1]
            [2 2 2 2 2 2 2 2 2 2]]

           (extract-rows
            [[0 0 0 0 0 0 0 0 0 0]
             [1 1 1 1 1 1 1 1 1 1]
             [2 2 2 2 2 2 2 2 2 2]
             [3 3 3 3 3 3 3 3 3 3]]
            1 2))))
  )



(deftest merge-rows-test
  (testing "Two rows are merged by replacing zeros with 1s."
    (is (= [1 0 1 1] (merge-rows [1 0 1 1]
                                 [0 0 0 0]))))

  (testing "Two rows are merged by replacing zeros with 1s, and leaving 1s alone."
    (is (= [1 1 1 1] (merge-rows [1 0 1 1]
                                 [1 1 0 1]))))
  )


(deftest merge-n-rows-test
  (testing "Similar to merge-rows, but merge N rows."
    (is (= [[1 1 1] [1 1 1] [1 1 1]] (merge-n-rows [[1 0 0] [0 1 0] [0 0 1]]
                                                   [[1 1 1] [1 0 1] [1 1 1]]))))
  )


(deftest replace-rows-test
  (testing "Starting at the specified index, replace rows on an existing board."
    (is (= [[0 0 0 0 0 0 0 0 0 0]
            [1 1 1 1 1 1 1 1 1 1]
            [2 2 2 2 2 2 2 2 2 2]
            [3 3 3 3 3 3 3 3 3 3]
            [4 4 4 4 4 4 4 4 4 4]
            [5 5 5 5 5 5 5 5 5 5]
            [6 6 6 6 6 6 6 6 6 6]]

           (replace-rows
            [[0 0 0 0 0 0 0 0 0 0]
             [1 1 1 1 1 1 1 1 1 1]
             [22 22 22 22 22 22 22 22 22 22]
             [33 33 33 33 33 33 33 33 33 33]
             [44 44 44 44 44 44 44 44 44 44]
             [5 5 5 5 5 5 5 5 5 5]
             [6 6 6 6 6 6 6 6 6 6]]
            2
            [[2 2 2 2 2 2 2 2 2 2]
             [3 3 3 3 3 3 3 3 3 3]
             [4 4 4 4 4 4 4 4 4 4]]))))
  )


;;; Assuming you have a board, with a padded letter added to the top, find where the letter
;;; should be placed when gravity is involved.  The number returned represents the row
;;; where the bottom most piece of the letter will be placed on the board - not necessarily
;;; touching the board if it's hooked on with it's top-post piece (e.g. S/Z/T do this).
(deftest letter-level-test
  (testing "Q should fall through to the bottom and touch the board at level 3."
    (is (= 3 (letter-level [[0 0 1 1 0 0 0 0 0 0]
                            [0 0 1 1 0 0 0 0 0 0]
                            [1 1 0 0 1 1 0 0 1 1]
                            [1 1 0 0 1 1 0 0 1 1]]
                           Q 2))))

  (testing "Q should hit a conflict at level 1."
    (is (= 1 (letter-level [[0 1 1 0 0 0 0 0 0 0]
                            [0 1 1 0 0 0 0 0 0 0]
                            [1 1 0 0 1 1 0 0 1 1]
                            [1 1 0 0 1 1 0 0 1 1]]
                           Q 1))))

  (testing "Q should hit a conflict at level 1."
    (is (= 1 (letter-level [[0 0 0 1 1 0 0 0 0 0]
                            [0 0 0 1 1 0 0 0 0 0]
                            [1 1 0 0 1 1 0 0 1 1]
                            [1 1 0 0 1 1 0 0 1 1]]
                           Q 3))))

  (testing "S should fall through to the bottom and touch the board at level 3."
    (is (= 3 (letter-level [[0 0 0 1 1 0 0 0 0 0]
                            [0 0 1 1 0 0 0 0 0 0]
                            [1 1 0 0 0 1 0 0 1 1]
                            [1 1 0 0 0 1 0 0 1 1]]
                           S 2))))

  (testing "S should hit a conflict at level 1."
    (is (= 1 (letter-level [[0 0 1 1 0 0 0 0 0 0]
                            [0 1 1 0 0 0 0 0 0 0]
                            [1 1 0 0 0 1 0 0 1 1]
                            [1 1 0 0 0 1 0 0 1 1]]
                           S 1))))

  (testing "S should hit a conflict at level 1."
    (is (= 2 (letter-level [[0 0 0 0 1 1 0 0 0 0]
                            [0 0 0 1 1 0 0 0 0 0]
                            [1 1 0 0 0 1 0 0 1 1]
                            [1 1 0 0 0 1 0 0 1 1]]
                           S 3))))

  (testing "Z should fall through to the bottom and touch the board at level 3."
    (is (= 3 (letter-level [[0 0 1 1 0 0 0 0 0 0]
                            [0 0 0 1 1 0 0 0 0 0]
                            [1 1 0 0 0 1 0 0 1 1]
                            [1 1 0 0 0 1 0 0 1 1]]
                           Z 2))))

  (testing "Z should hit a conflict at level 1."
    (is (= 2 (letter-level [[0 1 1 0 0 0 0 0 0 0]
                            [0 0 1 1 0 0 0 0 0 0]
                            [1 1 0 0 0 1 0 0 1 1]
                            [1 1 0 0 0 1 0 0 1 1]]
                           Z 1))))

  (testing "Z should hit a conflict at level 1."
    (is (= 1 (letter-level [[0 0 0 1 1 0 0 0 0 0]
                            [0 0 0 0 1 1 0 0 0 0]
                            [1 1 0 0 0 1 0 0 1 1]
                            [1 1 0 0 0 1 0 0 1 1]]
                           Z 3))))

  (testing "T should fall through to the bottom and touch the board at level 3."
    (is (= 3 (letter-level [[0 0 1 1 1 0 0 0 0 0]
                            [0 0 0 1 0 0 0 0 0 0]
                            [1 1 0 0 0 1 0 0 1 1]
                            [1 1 0 0 0 1 0 0 1 1]]
                           T 2))))

  (testing "T should hit a conflict at level 1."
    (is (= 2 (letter-level [[0 1 1 1 0 0 0 0 0 0]
                            [0 0 1 0 0 0 0 0 0 0]
                            [1 1 0 0 0 1 0 0 1 1]
                            [1 1 0 0 0 1 0 0 1 1]]
                           T 1))))

  (testing "T should hit a conflict at level 1."
    (is (= 2 (letter-level [[0 0 0 1 1 1 0 0 0 0]
                            [0 0 0 0 1 0 0 0 0 0]
                            [1 1 0 0 0 1 0 0 1 1]
                            [1 1 0 0 0 1 0 0 1 1]]
                           T 3))))

  (testing "L should fall through to the bottom and touch the board at level 4."
    (is (= 4 (letter-level [[0 0 1 0 0 0 0 0 0 0]
                            [0 0 1 0 0 0 0 0 0 0]
                            [0 0 1 1 0 0 0 0 0 0]
                            [1 1 0 0 1 1 0 0 1 1]
                            [1 1 0 0 1 1 0 0 1 1]]
                           L 2))))

  (testing "L should hit a conflict at level 2."
    (is (= 2 (letter-level [[0 1 0 0 0 0 0 0 0 0]
                            [0 1 0 0 0 0 0 0 0 0]
                            [0 1 1 0 0 0 0 0 0 0]
                            [1 1 0 0 1 1 0 0 1 1]
                            [1 1 0 0 1 1 0 0 1 1]]
                           L 1))))

  (testing "L should hit a conflict at level 2."
    (is (= 2 (letter-level [[0 0 0 1 0 0 0 0 0 0]
                            [0 0 0 1 0 0 0 0 0 0]
                            [0 0 0 1 1 0 0 0 0 0]
                            [1 1 0 0 1 1 0 0 1 1]
                            [1 1 0 0 1 1 0 0 1 1]]
                           L 3))))

  (testing "J should fall through to the bottom and touch the board at level 4."
    (is (= 4 (letter-level [[0 0 0 1 0 0 0 0 0 0]
                            [0 0 0 1 0 0 0 0 0 0]
                            [0 0 1 1 0 0 0 0 0 0]
                            [1 1 0 0 1 1 0 0 1 1]
                            [1 1 0 0 1 1 0 0 1 1]]
                           J 2))))

  (testing "J should hit a conflict at level 2."
    (is (= 2 (letter-level [[0 0 1 0 0 0 0 0 0 0]
                            [0 0 1 0 0 0 0 0 0 0]
                            [0 1 1 0 0 0 0 0 0 0]
                            [1 1 0 0 1 1 0 0 1 1]
                            [1 1 0 0 1 1 0 0 1 1]]
                           J 1))))

  (testing "J should hit a conflict at level 2."
    (is (= 2 (letter-level [[0 0 0 0 1 0 0 0 0 0]
                            [0 0 0 0 1 0 0 0 0 0]
                            [0 0 0 1 1 0 0 0 0 0]
                            [1 1 0 0 1 1 0 0 1 1]
                            [1 1 0 0 1 1 0 0 1 1]]
                           J 3))))
  )

;;; Sometimes, when a non-solid letter is moved down, it will have some of it's
;;; residual self left over.  So, we have the ability to zero out the places in
;;; it's hard path (i.e. the bottom most part of the letter).
(deftest zero-offset-letter-test
  (testing "Should place zeros in the offset and offset+1 cols for L"
    (is (= [[1 1 0 0 1 1]
            [1 1 0 0 1 1]
            [1 1 0 0 1 1]]
           (zero-offset-letter [[1 1 1 1 1 1]
                                [1 1 1 1 1 1]
                                [1 1 1 1 1 1]] L 2))))

  (testing "Should place zeros in the offset and offset+1 cols for J"
    (is (= [[1 1 0 0 1 1]
            [1 1 0 0 1 1]
            [1 1 0 0 1 1]]
           (zero-offset-letter [[1 1 1 1 1 1]
                                [1 1 1 1 1 1]
                                [1 1 1 1 1 1]] J 2))))

  (testing "Should place zeros in the offset and offset+1 cols for S"
    (is (= [[1 1 0 0 1 1]
            [1 1 0 0 1 1]
            [1 1 0 0 1 1]]
           (zero-offset-letter [[1 1 1 1 1 1]
                                [1 1 1 1 1 1]
                                [1 1 1 1 1 1]] S 2))))

  (testing "Should place zeros in the offset+1 and offset+2 cols for Z"
    (is (= [[1 1 1 0 0 1]
            [1 1 1 0 0 1]
            [1 1 1 0 0 1]]
           (zero-offset-letter [[1 1 1 1 1 1]
                                [1 1 1 1 1 1]
                                [1 1 1 1 1 1]] Z 2))))

  (testing "Should place zeros in the offset and offset+1 cols for T"
    (is (= [[1 1 1 0 1 1]
            [1 1 1 0 1 1]
            [1 1 1 0 1 1]]
           (zero-offset-letter [[1 1 1 1 1 1]
                                [1 1 1 1 1 1]
                                [1 1 1 1 1 1]] T 2))))
  )


(deftest calc-row-height-test
  (testing "Row height should be 2 when adding: Q0"
    (is (= 2 (calc-row-height "Q0" false))))

  (testing "Row height should be 4 when adding: Q0,Q1"
    (is (= 4 (calc-row-height "Q0,Q1" false))))

  (testing "Row height should be 0 when adding: Q0,Q2,Q4,Q6,Q8"
    (is (= 0 (calc-row-height "Q0,Q2,Q4,Q6,Q8" false))))

  (testing "Row height should be 2 when adding: Q0,Q2,Q4,Q6,Q8,Q1"
    (is (= 2 (calc-row-height "Q0,Q2,Q4,Q6,Q8,Q1" false))))

  (testing "Row height should be 4 when adding: Q0,Q2,Q4,Q6,Q8,Q1,Q1"
    (is (= 4 (calc-row-height "Q0,Q2,Q4,Q6,Q8,Q1,Q1" false))))

  (testing "Row height should be 1 when adding: I0,I4,Q8"
    (is (= 1 (calc-row-height "I0,I4,Q8" false))))

  (testing "Row height should be 0 when adding: I0,I4,Q8,I0,I4"
    (is (= 0 (calc-row-height "I0,I4,Q8,I0,I4" false))))

  (testing "Row height should be 2 when adding: L0,J2,L4,J6,Q8"
    (is (= 2 (calc-row-height "L0,J2,L4,J6,Q8" false))))

  (testing "Row height should be 2 when adding: L0,Z1,Z3,Z5,Z7"
    (is (= 2 (calc-row-height "L0,Z1,Z3,Z5,Z7" false))))

  (testing "Row height should be 2 when adding: T0,T3"
    (is (= 2 (calc-row-height "T0,T3" false))))

  (testing "Row height should be 1 when adding: T0,T3,I6,I6"
    (is (= 1 (calc-row-height "T0,T3,I6,I6" false))))

  (testing "Row height should be 1 when adding: I0,I6,S4"
    (is (= 1 (calc-row-height "I0,I6,S4" false))))

  (testing "Row height should be 4 when adding: T1,Z3,I4"
    (is (= 4 (calc-row-height "T1,Z3,I4" false))))

  (testing "Row height should be 3 when adding: L0,J3,L5,J8,T1"
    (is (= 3 (calc-row-height "L0,J3,L5,J8,T1" false))))

  (testing "Row height should be 1 when adding: L0,J3,L5,J8,T1,T6"
    (is (= 1 (calc-row-height "L0,J3,L5,J8,T1,T6" false))))

  (testing "Row height should be 2 when adding: L0,J3,L5,J8,T1,T6,J2,L6,T0,T7"
    (is (= 2 (calc-row-height "L0,J3,L5,J8,T1,T6,J2,L6,T0,T7" false))))

  (testing "Row height should be 1 when adding: L0,J3,L5,J8,T1,T6,J2,L6,T0,T7,Q4"
    (is (= 1 (calc-row-height "L0,J3,L5,J8,T1,T6,J2,L6,T0,T7,Q4" false))))

  (testing "Row height should be 8 when adding: S0,S2,S4,S6"
    (is (= 8 (calc-row-height "S0,S2,S4,S6" false))))

  (testing "Row height should be 8 when adding: S0,S2,S4,S5,Q8,Q8,Q8,Q8,T1,Q1,I0,Q4"
    (is (= 8 (calc-row-height "S0,S2,S4,S5,Q8,Q8,Q8,Q8,T1,Q1,I0,Q4" false))))

  (testing "Row height should be 0 when adding: L0,J3,L5,J8,T1,T6,S2,Z5,T0,T7"
    (is (= 0 (calc-row-height "L0,J3,L5,J8,T1,T6,S2,Z5,T0,T7" false))))
  )
