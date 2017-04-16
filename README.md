# num-text

A trivial Clojure library designed to output numbers as text - using modern British units and grammar.


## Objective

We should be able to emit number in the following style

```clojure
(num->text 123456789)
=> "One hundred and twenty three million four hundred and fifty six thousand seven hundred and eighty nine"
```

## Explanation

See the blog post [link] explaining why I took this approach in more detail.

## Usage


## Testing


## Spec


## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.


## Breaking down the problem

### Groups of three

When we have a group of three numbers we should express them as:
 
- X hundred and Y singular UNIT

Where UNIT can be a value or nil

- 205 000 = two hundred and five thousand

- 205 = two hundred and five

We know that if the number is > 1000 the UNIT will be set.

### Groups of one or two

When the number is in between the unit ranges - so 10<sup>4</sup> and 10<sup>5</sup> for example - the first number should be expressed in the same way as singular numbers

### Deducing number ranges

The common choices I have seen here are to print the number into a string and count the digits.

This works but is hackish - it places the number into an impoverished and hostile environment.

Why not compare the number against a collection of number ranges.

### Hard coding text / number mappings

This seems awkward as we know that computers can generate number sequences. 

Using lists of keywords we can use symbols as a stand in for text. While this may seem more complex, it's actually simpler.

It gives us many affordances for checking general constraints of the solution that would be much more complex with strings.

Example: all odd numbers end with an odd number between 1 - 19. 

(map #(get singles %) (filter odd? (keys singles)))
=> (:seven :one :fifteen :thirteen :seventeen :three :nineteen :eleven :nine :five)
