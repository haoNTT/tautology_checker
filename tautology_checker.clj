;; Part I helper functions
(def op
	(fn [p]
		(first p)))

(def left-arg
	(fn [p]
		(first (rest p))))

(def right-arg
	(fn [p]
		(first (rest (rest p)))))

;; Main function 1
(def ifify
	(fn [p]
		(cond 
			(not (list? p)) p
			(= (op p) (quote not)) (list 'if (ifify (left-arg p)) 'false 'true)
			(= (op p) (quote or)) (list 'if (ifify (left-arg p)) 'true (ifify (right-arg p)))
			(= (op p) (quote and)) (list 'if (ifify (left-arg p)) (ifify (right-arg p)) 'false)
			(= (op p) (quote imply)) (list 'if (ifify (left-arg p)) (ifify (right-arg p)) 'true)
			(= (op p) (quote equiv)) (list 'if (ifify (left-arg p)) (ifify (right-arg p)) (list 'if (ifify (right-arg p)) 'false 'true)))))

;; Part II helper functions
(def make-if
	(fn [a b c]
		(list 'if a b c)))

(def if-test
	(fn [p]
		(if
			(list? p)
			(first (rest p))
			p)))

(def if-then
	(fn [p]
		(if 
			(list? p)
			(first (rest (rest p)))
			p)))

(def if-else
	(fn [p]
		(if
			(list? p)
			(first (rest (rest (rest p))))
			p)))
;; Main function 2

(def normalize
	(fn [c]
		(if
			(not (list? c))
			c
			(if
				(not (list? (if-test c)))
				(make-if (if-test c) (normalize (if-then c)) (normalize (if-else c)))
				(normalize (make-if (if-test (if-test c)) (make-if (if-then (if-test c)) (if-then c) (if-else c)) 
				(make-if (if-else (if-test c)) (if-then c) (if-else c))))))))

;; PartIII helper functions
(def simplify1 
	(fn [p]
		(if-then p)))

(def simplify2
	(fn [p]
		(if-else p)))

(def simplify3
	(fn [p]
		(if-test p)))


(def constructor
	(fn [c v b]
		(if
			(= c v)
			b
			c)))

(def substitute 
	(fn [c v b]
		(if
			(list? c)
			(if
				(and (list? (if-then c))
					(list? (if-else c)))
				(list 'if (constructor (if-test c) v b) (substitute (if-then c) v b) (substitute (if-else c) v b))
				(if
					(and (not (list? (if-then c)))
						(list? (if-else c)))
					(list 'if (constructor (if-test c) v b) (constructor (if-then c) v b) (substitute (if-else c) v b))
					(if 
						(and (list? (if-then c))
							(not (list? (if-else c))))
						(list 'if (constructor (if-test c) v b) (substitute (if-then c) v b) (constructor (if-else c) v b))
						(list 'if (constructor (if-test c) v b) (constructor (if-then c) v b) (constructor (if-else c) v b)))))
			(constructor c v b))))

(def canSimplify? 
	(fn [c]
		(if
			(list? c)
			(if
				(or
					(= (if-test c) true)
					(= (if-test c) false)
					(= (if-then c) (if-else c))
					(and (= (if-then c) true)
						(= (if-else c) false)))
				true
				false)
			false)))

(def canSubstitute?
	(fn [c check]
		(if 
			(empty? c)
			false
			(if
				(list? (first c))
				(if
					(canSubstitute? (first c) check)
					true
					(canSubstitute? (rest c) check))
				(if
					(= check (first c))
					true
					(canSubstitute? (rest c) check))))))

(declare simplify)
(def wrap-up2
	(fn [c]
		(if
			(list? c)
			(if 
				(= (if-test c) true)
				(simplify (simplify1 c))
				(if 
					(= (if-test c) false)
					(simplify (simplify2 c))
					(if
						(and (= (if-then c) true)
							(= (if-else c) false))
						(simplify (simplify3 c))
						(simplify (simplify1 c)))))
			c)))
							
							
; Main function 3
(def simplify 
	(fn [c]
		(if
			(list? c)
			(if
				(canSimplify? c)
				(simplify (wrap-up2 c))
				(if
					(canSubstitute? (rest (rest c)) (if-test c))
					(simplify (list 'if (if-test c) (simplify (substitute (if-then c) (if-test c) true)) (simplify (substitute (if-else c) (if-test c) false))))
					(if
						(or (list? (if-then c)) (list? (if-else c)))
						(list 'if (if-test c) (simplify (if-then c)) (simplify (if-else c)))
						c)))
			c)))

;; Mian function 4
(def tautology? 
	(fn [p]
		(let [result (simplify (normalize (ifify p)))]
			(if
				(= result true)
				true
				false))))

