# num-text

A trivial Clojure library designed to output numbers as text - UK English style.

## Usage

How should we emit the following numbers?
 
|number|text|
|----| ---|
| 8 | eight|
| 79 | seventy nine|
| 619 | six hundred and nineteen|

5 919 
- five thousand, nine hundred and nineteen

49 191

- forty nine thousand, one hundred and ninety one

391 919
- three hundred and ninety one thousand, nine hundred and nineteen

2 919 191 
- two million, nine hundred and nineteen thousand, one hundred and ninety one
 
89 191 919
- eighty nine million, one hundred and ninety one thousand, nine hundred and nineteen




Should we express this number using the first one or two digits?

19191 919 - should be 19 million, remainder 191 919
2919 191 - should be 2 million, remainder 919 191
391919 - should be 3 hundred, remainder 91 919
      - three hundred and ninety one thousand nine hundred and nineteen
49191 - should be 49 thousand, remainder 191
     - forty nine thousand one hundred and ninety one
5919 - should be 5 thousand remainder 919
    - five thousand nine hundred and nineteen
619 - should be 6 hundred remainder 19
   - six hundred and nineteen
79 - should be 79
  - seventy nine
8 - should be 8
 - eight"

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
