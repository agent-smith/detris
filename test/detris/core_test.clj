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

(deftest pad-test
  (testing "Pads the given vector with zeros, expanding the vector to the total."
    (is (= [0 0 1 1 0 0 0 0 0 0] (pad [1 1] 10 2)))))

(deftest remove-last-test
  (testing "Removes the last element in a vector."
    (is (= [[1 2 3] [4 5 6]] (remove-last [[1 2 3] [4 5 6] [7 8 9]])))))

(deftest add-letter-to-top-test
  (testing "Adding letter to top of existing board."
    (is (= [[0 0 0 1 0 0 0 0 0 0]
            [0 0 0 1 0 0 0 0 0 0]
            [0 0 1 1 0 0 0 0 0 0]
            [0 0 0 1 1 1 1 1 1 1]
            [0 1 1 0 0 0 0 0 0 0]]

           (add-letter-to-top
            [[0 0 0 1 1 1 1 1 1 1]
             [0 1 1 0 0 0 0 0 0 0]]
            J 2)))))

(deftest replace-in-row-test
  (testing "Should replace test row with 1s in correct positions."
    (is (= [1 1 0 0] (replace-in-row [0 0 0 0] [1 1] 0)))
    (is (= [0 1 1 0] (replace-in-row [0 0 0 0] [1 1] 1)))
    (is (= [0 0 1 1] (replace-in-row [0 0 0 0] [1 1] 2)))))

(deftest row-conflict-test
  (testing "True if 2 vectors have overlapping 1s, otherwise false."
    (is (= true (row-conflict? [0 0 0 0 1 1 0 0 0 0] [0 0 0 0 0 1 1 0 0 0])))
    (is (= false (row-conflict? [0 0 0 0 0 0 0 0 0 0] [1 1 0 0 0 0 0 0 0 0])))))

(deftest place-on-board-test
  (testing "Adding letter to an existing board."
    (is (= [[0 0 1 0 0 0 0 0 0 0]
            [0 0 1 0 0 0 0 0 0 0]
            [1 1 1 0 0 0 0 0 0 0]
            [0 1 0 0 1 1 0 0 0 0]
            [0 0 0 0 0 0 0 0 0 0]]

           (place-on-board
            [[0 0 0 0 0 0 0 0 0 0]
             [1 0 0 0 0 0 0 0 0 0]
             [0 1 0 0 1 1 0 0 0 0]
             [0 0 0 0 0 0 0 0 0 0]]
            J 1 0)))))

(deftest calc-row-height-test
  (testing "Row height should be 2 when adding: Q0"
    (is (= 2 (calc-row-height "Q0" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 4 when adding: Q0,Q1"
    (is (= 4 (calc-row-height "Q0,Q1" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 0 when adding: Q0,Q2,Q4,Q6,Q8"
    (is (= 0 (calc-row-height "Q0,Q2,Q4,Q6,Q8" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 2 when adding: Q0,Q2,Q4,Q6,Q8,Q1"
    (is (= 2 (calc-row-height "Q0,Q2,Q4,Q6,Q8,Q1" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 4 when adding: Q0,Q2,Q4,Q6,Q8,Q1,Q1"
    (is (= 4 (calc-row-height "Q0,Q2,Q4,Q6,Q8,Q1,Q1" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 0 when adding: I0,I4,Q8"
    (is (= 0 (calc-row-height "I0,I4,Q8" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 1 when adding: I0,I4,Q8,I0,I4"
    (is (= 1 (calc-row-height "I0,I4,Q8,I0,I4" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 2 when adding: L0,J2,L4,J6,Q8"
    (is (= 2 (calc-row-height "L0,J2,L4,J6,Q8" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 3 when adding: L0,Z1,Z3,Z5,Z7"
    (is (= 3 (calc-row-height "L0,Z1,Z3,Z5,Z7" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 2 when adding: T0,T3"
    (is (= 2 (calc-row-height "T0,T3" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 2 when adding: T0,T3,I6,I6"
    (is (= 2 (calc-row-height "T0,T3,I6,I6" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 1 when adding: I0,I6,S4"
    (is (= 1 (calc-row-height "I0,I6,S4" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 4 when adding: T1,Z3,I4"
    (is (= 4 (calc-row-height "T1,Z3,I4" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 4 when adding: L0,J3,L5,J8,T1"
    (is (= 4 (calc-row-height "L0,J3,L5,J8,T1" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 4 when adding: L0,J3,L5,J8,T1,T6"
    (is (= 4 (calc-row-height "L0,J3,L5,J8,T1,T6" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 6 when adding: L0,J3,L5,J8,T1,T6,J2,L6,T0,T7"
    (is (= 6 (calc-row-height "L0,J3,L5,J8,T1,T6,J2,L6,T0,T7" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 6 when adding: L0,J3,L5,J8,T1,T6,J2,L6,T0,T7,Q4"
    (is (= 6 (calc-row-height "L0,J3,L5,J8,T1,T6,J2,L6,T0,T7,Q4" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 8 when adding: S0,S2,S4,S6"
    (is (= 8 (calc-row-height "S0,S2,S4,S6" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 9 when adding: S0,S2,S4,S5,Q8,Q8,Q8,Q8,T1,Q1,I0,Q4"
    (is (= 9 (calc-row-height "S0,S2,S4,S5,Q8,Q8,Q8,Q8,T1,Q1,I0,Q4" false)))))

(deftest calc-row-height-test
  (testing "Row height should be 7 when adding: L0,J3,L5,J8,T1,T6,S2,Z5,T0,T7"
    (is (= 7 (calc-row-height "L0,J3,L5,J8,T1,T6,S2,Z5,T0,T7" false)))))
