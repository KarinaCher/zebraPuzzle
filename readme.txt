Solution contains form two steps:

1. step
Check all direct conditions like:
- The Norwegian lives in the first house.
- The Norwegian lives next to the blue house.
 
after check all this conditions we have such result:
house nr     | 1         | 2      | 3    | 4   | 5
color        | yellow    | blue   | ?    | ?   | ?
nationality  | norvegian | ?      | ?    | ?   | ?
drink        | water     | ?      | milk | ?   | ?
smoke        | Kool      | ?      | ?    | ?   | ?
pet          | ?         | horse  | ?    | ?   | ?

2. step
Do assumption and check if conditions has logical error. If has error then do other assumption. If hasn't errors, then this is puzzle right answer.

Program get 3 arguments:
1st argument: count of objects (houses) - "There are five houses."

2nd file with condition.
One line per condition. 
Two types of conditions: SAME - both conditions for same house, NEXT - first condition for house(i), second for house(i + 1).

conditons:
The Englishman lives in the red house.
The Spaniard owns the dog.
Coffee is drunk in the green house.
The Ukrainian drinks tea.
The green house is immediately to the right of the ivory house.
The Old Gold smoker owns snails.
Kools are smoked in the yellow house.
Milk is drunk in the middle house.
The Norwegian lives in the first house.
The man who smokes Chesterfields lives in the house next to the man with the fox.
Kools are smoked in the house next to the house where the horse is kept.
The Lucky Strike smoker drinks orange juice.
The Japanese smokes Parliaments.
The Norwegian lives next to the blue house.

file:
SAME;nationality;English;color;Red
SAME;nationality;Spaniard;pet;Dog
SAME;drink;Coffee;color;Green
SAME;drink;Tea;nationality;Ukrainian
NEXT;color;Ivory;color;Green
SAME;smoke;Old gold;pet;Snails
SAME;smoke;Kools;color;Yellow
SAME;drink;Milk;position;3
SAME;nationality;Norwegian;position;1
NEXT;pet;Fox;smoke;Chesterfields
NEXT;smoke;Kools;pet;Horse
SAME;smoke;Lucky strike;drink;Orange juice
SAME;smoke;Parliaments;nationality;Japanese
NEXT;nationality;Norwegian;color;Blue

3rd question file "Now, who drinks water? Who owns the zebra?".
pet;Zebra
drink;Water

Comment:
I know that exception using in programm logic is undesirable, but here it illustrate algorithm principle: when we find if our assumption contain exceptions means that assumption is wrong. This give us clear understanding of algorithm principle.
