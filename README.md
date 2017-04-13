# num-text

A trivial Clojure library designed to output numbers as text - using modern British units and grammar.


## Objective

We should be able to emit number in the following style
 
|number|text|
|----| ---|
| 8 | eight|
| 79 | seventy nine|
| 619 | six hundred and nineteen|
| 5 919 | five thousand, nine hundred and nineteen |
| 49 191 | forty nine thousand, one hundred and ninety one |
| 391 919 | three hundred and ninety one thousand, nine hundred and nineteen |
| 2 919 191 | two million, nine hundred and nineteen thousand, one hundred and ninety one |
| 89 191 919 | eighty nine million, one hundred and ninety one thousand, nine hundred and nineteen |


## Breaking down the problem

Some aspects of this text are repeated:

## Unit changes

|Value|Name|
|-----|----|
|0-100|singular|
|10<sup>2</sup>|hundred|
|10<sup>3</sup>|thousand|
|10<sup>6</sup>|million|
|10<sup>9</sup>|billion|
|... |...|

After thousands the unit changes on the third increase of the exponent.

If we can deduce the exponent of the number, we can assign it to the proper name. 

## Groups of three

When we have a group of three numbers we should express them as:
 
- X hundred and Y singular UNIT

Where UNIT can be a value or nil

- 205 000 = two hundred and five thousand

- 205 = two hundred and five

We know that if the number is > 1000 the UNIT will be set.

## Groups of one or two

When the number is in between the unit ranges - so 10<sup>4</sup> and 10<sup>5</sup> for example - the first number should be expressed in the same way as singular numbers

## Deducing number ranges

The common choices I have seen here are to print the number into a string and count the digits.

This works but seems hackish.

I would prefer to be able to compare the number against a collection of number ranges.




## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
