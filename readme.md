# R Tree

> An implementation of paper [《R-trees: a dynamic index structure for spatial searching》](https://dl.acm.org/doi/pdf/10.1145/602259.602266) which supports n-dimension spatial indexing.
>



### Search

For 2-dimension spatial indexing, the bound vector here is the minimum bounding rectangle(MBR).

For n-dimension spatial indexing, the bound vector here is the minimum bounding n-dimension-vector.

```java
@Test
public void search() {
        BoundVector boundVector = new BoundVector(DIMENSION);
        int value = RandomUtil.getIntegerRandomNumber(DIMENSION, 100);
        for (int i = 0; i < DIMENSION; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        Set<RTreeEntry<Integer>> results =  rTree.search(new Rectangle(boundVector));
}
```



### Insert

```java
@Test   
public void insert() {
        for (int i = 0; i < 10; ++i) {
            RTreeEntry<Integer> entry = generateRandomEntry();
            rTree.insert(entry);
        }
}
    
    
private RTreeEntry<Integer> generateRandomEntry() {
        int value = RandomUtil.getIntegerRandomNumber(DIMENSION, 100);
        BoundVector boundVector = new BoundVector(DIMENSION);
        for (int i = 0; i < DIMENSION; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        Rectangle rectangle = new Rectangle(boundVector);
        return new RTreeEntry<>(rectangle, value);
}
```



### Update

```java
@Test
public void update() {
        BoundVector boundVector = new BoundVector(DIMENSION);
        int value = RandomUtil.getIntegerRandomNumber(DIMENSION, 100);
        for (int i = 0; i < DIMENSION; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        Set<RTreeEntry<Integer>> results =  rTree.search(new Rectangle(boundVector));
        RTreeEntry<Integer> rTreeEntry = results.stream().findAny().orElseThrow(() -> new RuntimeException("find nothing"));
        rTree.update(rTreeEntry.getId(),RandomUtil.getIntegerRandomNumber());
}
```



### Delete

```java
@Test
public void delete() {
        int value = RandomUtil.getIntegerRandomNumber(DIMENSION, 100);
        BoundVector boundVector = new BoundVector(DIMENSION);
        for (int i = 0; i < DIMENSION; ++i) {
            boundVector.setDimensionBound(i, new Bound(value - 1 - i, value + 1 + i));
        }
        RTreeEntry<Integer> rTreeEntry = new RTreeEntry<>(new Rectangle(boundVector),value);
        System.out.println(rTree.delete(rTreeEntry));
}
```



### Visualization

A  implementation of R tree rectangle's visualization in RTreeUtil(only available for 2-dimension)

~~~java
@Test
public void testVisualization(){
        insert();
        RTreeUtil.printRTreeRectangle(rTree);
}

//The console output is as follows

``````````````````````````````````````````````
``````   `                                   `
``````   `                                   `
``````   `                                   `
`        `                                   `
`    `````                                   `
`    `   `                                   `
``````````                                   `
`                                            `
`                                            `
`         ``````````                         `
`         `   `    `                         `
`         `````    `                         `
`         `        `                         `
`         `   ``````                         `
`         `   ``````                         `
`         `   ``````                         `
`         ``````````                         `
`                                            `
`                  ``````                    `
`                  ``````                    `
`                  ``````                    `
`                  ``````                    `
`                                            `
`                       ```````````          `
`                       ``````    `          `
`                       ``````    `          `
`                       ````````  `          `
`                       `  `   `  `          `
`                       `  `````  `          `
`                       `     `````          `
`                       `     `   `          `
`                       ```````````          `
`                                            `
`                                 ````````````
`                                 `   `      `
`                                 ```````    `
`                                 ` `   `    `
`                                 ` `````    `
`                                 `          `
`                                 `     ``````
`                                 `     ``````
`                                 `     ``````
``````````````````````````````````````````````
~~~



You can find the above code in [RTreeTest](https://github.com/Morgan279/R-Tree/blob/main/src/test/java/edu/ecnu/wclong/RTreeTest.java).



### Node Splitting

I implement the  Quadratic-Cost Algorithm and Linear-Cost Algorithm that mentioned in the paper.

##### Quadratic-Cost Algorithm

```java
public class RTreeNodeQuadraticSeparator implements RTreeNodeSeparator {

    @Override
    public <T> PickSeedsResult<T> pickSeeds(List<RTreeEntry<T>> entries) {
        RTreeEntry<T> seed1 = null;
        RTreeEntry<T> seed2 = null;
        double largestDiff = -Double.MAX_VALUE;

        for (int i = 0; i < entries.size(); ++i) {
            for (int j = 0; j < entries.size(); ++j) {
                if (i == j) continue;

                Rectangle rectangle1 = entries.get(i).getRectangle();
                Rectangle rectangle2 = entries.get(j).getRectangle();

                double currentDiff = RectangleUtil.getBoundedRectangleByTwoRectangles(
                        rectangle1,
                        rectangle2
                ).getArea() - rectangle1.getArea() - rectangle2.getArea();

                if (currentDiff > largestDiff) {
                    seed1 = entries.get(i);
                    seed2 = entries.get(j);
                    largestDiff = currentDiff;
                }
            }
        }

        return new PickSeedsResult<>(seed1, seed2);
    }

    @Override
    public <T> RTreeEntry<T> pickNext(List<RTreeEntry<T>> entries, List<RTreeEntry<T>> splitList1, List<RTreeEntry<T>> splitList2) {
        RTreeEntry<T> nextEntry = entries.get(0);
        double maxDiff = RTreeNodeSplitUtil.calculateAreaIncreaseDiff(nextEntry, splitList1, splitList2);
        for (int i = 1; i < entries.size(); ++i) {
            double diff = RTreeNodeSplitUtil.calculateAreaIncreaseDiff(entries.get(i), splitList1, splitList2);
            if (diff > maxDiff) {
                nextEntry = entries.get(i);
            }
        }
        return nextEntry;
    }
    
}
```



##### Linear-Cost Algorithm

```java
public class RTreeNodeLinearSeparator implements RTreeNodeSeparator {

    @Override
    public <T> PickSeedsResult<T> pickSeeds(List<RTreeEntry<T>> entries) {
        int dimension = entries.get(0).getRectangle().getDimension();

        double[] highestLowSide = new double[dimension];
        double[] lowestHighSide = new double[dimension];
        double[] dimensionWidth = new double[dimension];
        Map<Integer, RTreeEntry<T>> highestLowSideEntryMap = new HashMap<>();
        Map<Integer, RTreeEntry<T>> lowestHighSideEntryMap = new HashMap<>();

        for (int i = 0; i < dimension; ++i) {
            highestLowSide[i] = -Double.MAX_VALUE;
            lowestHighSide[i] = Double.MAX_VALUE;
            dimensionWidth[i] = 0;

            for (RTreeEntry<T> entry : entries) {
                Bound bound = entry.getRectangle().getBoundByIndex(i);
                dimensionWidth[i] += bound.getHigherBound() - bound.getLowerBound();

                if (bound.getLowerBound() > highestLowSide[i]) {
                    highestLowSide[i] = bound.getLowerBound();
                    highestLowSideEntryMap.put(i, entry);
                }

                if (bound.getHigherBound() < lowestHighSide[i]) {
                    lowestHighSide[i] = bound.getHigherBound();
                    lowestHighSideEntryMap.put(i, entry);
                }
            }
        }

        int extremeSeparationDimension = 0;
        double extremeSeparation = Math.abs(highestLowSide[0] - lowestHighSide[0]) / dimensionWidth[0];
        for (int i = 1; i < dimension; ++i) {
            double separation = Math.abs(highestLowSide[i] - lowestHighSide[i]) / dimensionWidth[i];
            if (separation > extremeSeparation) {
                extremeSeparation = separation;
                extremeSeparationDimension = i;
            }
        }

        return new PickSeedsResult<>(
                highestLowSideEntryMap.get(extremeSeparationDimension),
                lowestHighSideEntryMap.get(extremeSeparationDimension)
        );
    }

    @Override
    public <T> RTreeEntry<T> pickNext(List<RTreeEntry<T>> entries, List<RTreeEntry<T>> splitList1, List<RTreeEntry<T>> splitList2) {
        return entries.stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("entries cannot be empty"));
    }

}
```



You can also design your own node splitting algorithm by implementing the interface RTreeNodeSeparator

```java
public interface RTreeNodeSeparator {

    <T> PickSeedsResult<T> pickSeeds(List<RTreeEntry<T>> entries);

    <T> RTreeEntry<T> pickNext(List<RTreeEntry<T>> entries, List<RTreeEntry<T>> splitList1, List<RTreeEntry<T>> splitList2);

}
```







------

### Contact Me

Please feel free to contact me if you have any questions: coder_wclong@163.com.