# tautology_checker
This is a functional programming project which is designed to check if a certain propositional statement is a tautology. Theoretically, an expression is called tautology if it is true for all possible variables. As a branch of mathematics, propositional logocs has been widely used in some edge-cutting academic fielsd such as artificial intelligence. 

The purpose of this project is to present a demo of the realization of tautology checker in functional programming. The most typical tautology is the one which can be easily generated from De Morgan's Law. For instance, the following expression can always yield a true: [not (p and q)] -> [(not p) or (not q)].

In this project, the programming language Clojure will be used to check if an input expression is a tautology. Since there is no corresponding symbols in Clojure to represent boolean operators, the project first defines a sequence of symbols which are used to represent certain boolean expressions. Specifically, we define symbols 'not' to represent logic not, 'and' stands for logic and, 'or' for logic or, 'imply' for logic imply and 'equiv' for logic equivalence.

The other important technique applied in this project is the trick to translate input boolean expressions to functional programming expressions. Logic if is a very essential and frequently used logic expression in functional programming and in this project, we utilize the if statement to translate logic expressions to functional programming expressions. The typical instances can be presented as follows:

1.(not α) ⇒ (if α false true)

2.(and α β) ⇒ (if α β false)

3.(or α β) ⇒ (if α true β)

4.(imply α β) ⇒ (if α β true)

5.(equiv α β) ⇒ (if α β (if β false true))
