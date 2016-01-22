(ns game-of-life.core-test
  (:require [clojure.test :refer :all]
            [game-of-life.core :refer :all]))

(deftest alive-dead-test
  (is (alive? [0 0] (world [0 0])))
  (is (dead? [-1 0] (world [0 0])))

  (are [x y _world] (and
                     (alive? [x y] _world)
                     (not (dead? [x y] _world)))
       0 0 (world [0 0])
       1 1 (world [1 1] [-1 0])
       -1 0 (world [1 1] [-1 0]))

  (are [x y _world] (and
                     (dead? [x y] _world)
                     (not (alive? [x y] _world)))
       0 0 (world [2 3])
       1 5 (world [2 3])
       -10 2 (world [2 3])
       3 -6 (world [2 3])
       4 1 (world)))

(deftest neighbours-count-test
  (is (= 0 (count (neighbours [0 0] (world)))))
  (is (= 0 (count (neighbours [0 0] (world [0 0])))))
  (is (= 1 (count (neighbours [1 1] (world [0 0] [1 1])))))
  (is (= 2 (count (neighbours [1 1] (world [0 0] [0 1])))))
  (is (= 5 (count (neighbours [2 3] (world [0 0] [1 1] [2 3] [1 2] [2 2] [3 3] [3 4] [1 4])))))
  (is (= 8 (count (neighbours [11 11] (world [10 10] [10 11] [10 12]
                                             [11 10] [11 11] [11 12]
                                             [12 10] [12 11] [12 12])))))
  )

(deftest will-live-test
  (testing "Rule #1: live cell with fewer than two live neighbours dies"
    (is (not (will-live? [11 11] (world [11 10] [11 11])))))

  (testing "Rule #2: live cell with two or three live neighbours lives"
    (is (will-live? [11 11] (world [11 10] [11 11] [11 12])))
    (is (will-live? [11 11] (world [11 10] [11 11] [11 12] [12 10]))))

  (testing "Rule #3: live cell with more than three live neighbours dies"
    (is (not (will-live? [11 11] (world [10 10] [10 11]
                                                [11 11] [11 12]
                                                [12 11]        ))))
    (is (not (will-live? [11 11] (world [10 10] [10 11] [10 12]
                                        [11 10] [11 11] [11 12]
                                        [12 10] [12 11] [12 12])))))
  
  (testing "Rule #4: dead cell with exactly three live neighbours becomes a live cell"
    (is (will-live? [11 11] (world [10 10] [10 11] [10 12])))))

(def glider-step-1 (world         [10 11]
                                          [11 12]
                          [12 10] [12 11] [12 12]))


(def glider-step-2 (world [11 10]         [11 12]
                                  [12 11] [12 12]
                                  [13 11]))


(def glider-step-3 (world                 [11 12]
                          [12 10]         [12 12]
                                  [13 11] [13 12]))


(def glider-step-4 (world         [11 11]
                                          [12 12] [12 13]
                                  [13 11] [13 12]))

(deftest step-test
  (testing "glider"
    (is (= (step glider-step-1) glider-step-2))
    (is (= (step glider-step-2) glider-step-3))
    (is (= (step glider-step-3) glider-step-4))))
