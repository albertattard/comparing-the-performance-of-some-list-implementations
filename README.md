A question that developers ask is: with many <code>List</code> (<a href="http://docs.oracle.com/javase/7/docs/api/java/util/List.html" target="_blank">Java Doc</a>) implementations, which implementation should a developer uses?  The correct answer to this question is: it depends.  All implementations serve a different purpose and one cannot simply say that a particular implementation is always better than the others, in all cases.  One can say that one implementation may be more suitable in a particular situation than others or that an implementation will perform slower than the others in certain circumstances.


In this article we measure the time take required by various implementations to perform a set of specific actions.  We will consider the following list implementations in the experiment:


<ul>
<li><code>Vector</code> (<a href="http://docs.oracle.com/javase/7/docs/api/java/util/Vector.html" target="_blank">Java Doc</a>)</li>
<li><code>Vector</code> (with size set at construction time)</li>
<li><code>ArrayList</code> (<a href="http://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html" target="_blank">Java Doc</a>)</li>
<li><code>ArrayList</code> (with size set at construction time)</li>
<li><code>LinkedList</code> (<a href="http://docs.oracle.com/javase/7/docs/api/java/util/LinkedList.html" target="_blank">Java Doc</a>)</li>
<li><code>Stack</code> (<a href="http://docs.oracle.com/javase/7/docs/api/java/util/Stack.html" target="_blank">Java Doc</a>)</li>
<li><code>CopyOnWriteArrayList</code> (<a href="http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CopyOnWriteArrayList.html" target="_blank">Java Doc</a>)</li>
</ul>


During the tests we will perform the following actions on each instance:
<ul>
<li><code>List.add(Object)</code></li>
<li><code>List.get(int)</code></li>
<li><code>List.iterator()</code></li>
<li><code>List.size()</code></li>
</ul>


All tests are carried on a single thread and concurrency is not taken into account.  The test is designed to be extendable and we can add as many <code>List</code> implementations as we want in order to increase the test coverage.  Furthermore, we can also include new tests to cover more <code>List</code> actions.  


All code listed below is available at: <a href="https://github.com/javacreed/comparing-the-performance-of-some-list-implementations" target="_blank">https://github.com/javacreed/comparing-the-performance-of-some-list-implementations</a>.  Most of the examples will not contain the whole code and may omit fragments which are not relevant to the example being discussed.  The readers can download or view all code from the above link.


<h2>Results</h2>


The output of the test is formatted as shown in the following matrix.


<table>
<tbody>
<tr>
<th>List Type</th>
<th style='text-align:right'>add()</th>
<th style='text-align:right'>get()</th>
<th style='text-align:right'>iterate()</th>
<th style='text-align:right'>size()</th>
</tr>
<tr>
<td>Vector</td>
<td style='text-align:right'>12.691</td>
<td style='text-align:right'>0.143</td>
<td style='text-align:right'>0.286</td>
<td style='text-align:right'>0.047</td>
</tr>
<tr>
<td>Vector with init size</td>
<td style='text-align:right'>10.134</td>
<td style='text-align:right'>0.045</td>
<td style='text-align:right'>0.042</td>
<td style='text-align:right'>0.009</td>
</tr>
<tr>
<td>ArrayList</td>
<td style='text-align:right'>9.873</td>
<td style='text-align:right'>0.051</td>
<td style='text-align:right'>0.037</td>
<td style='text-align:right'>0.013</td>
</tr>
<tr>
<td>ArrayList with init size</td>
<td style='text-align:right'>9.845</td>
<td style='text-align:right'>0.036</td>
<td style='text-align:right'>0.003</td>
<td style='text-align:right'>0.005</td>
</tr>
<tr>
<td>LinkedList</td>
<td style='text-align:right'>9.913</td>
<td style='text-align:right'>172.824</td>
<td style='text-align:right'>0.538</td>
<td style='text-align:right'>0.030</td>
</tr>
<tr>
<td>Stack</td>
<td style='text-align:right'>9.843</td>
<td style='text-align:right'>0.105</td>
<td style='text-align:right'>0.129</td>
<td style='text-align:right'>0.060</td>
</tr>
<tr>
<td>CopyOnWriteArrayList</td>
<td style='text-align:right'>36.909</td>
<td style='text-align:right'>0.092</td>
<td style='text-align:right'>0.099</td>
<td style='text-align:right'>0.051</td>
</tr></tbody>
</table>


The above results are the average performance of 100 runs for a list with size of: 10000 and performing 10000 operations.  Therefore each operation was performed 10000 times on each list instance.  <strong>All time figures listed in this article are in milliseconds</strong>.


Each test is described in further details in the following sections.


<h2>Inserting Elements to a List</h2>

The first test that was performed involved populating the list with some generated strings as shown the following code fragment.


<pre>
  private final String pattern = "Element %d";

  @Override
  public long timeAction(List&lt;String&gt; list, int limit) {
    final long start = System.nanoTime();
    for (int i = 0; i &lt; limit; i++) {
      list.add(String.format(pattern, i));
    }
    return System.nanoTime() - start;
  }
</pre>


Most of the tested implementations obtained similar performance time with the exception of the <code>CopyOnWriteArrayList</code> implementation.  As documented in the class <a href="http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CopyOnWriteArrayList.html" target="_blank">Java Doc</a>, a new array is created when a new item is added to the list.


<blockquote cite="http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/CopyOnWriteArrayList.html">A thread-safe variant of <code>ArrayList</code> in which all mutative operations (<code>add</code>, <code>set</code>, and so on) are implemented by making a fresh copy of the underlying array.</blockquote>


Therefore, a new copy of the underlying array was created for every insert made during the test.  This explains why the insertions were considerably slower when compared with the other lists implementations.  Note that this implementation was designed to work with many threads and where the number reads are far greater than the number of writes.


The following graph provides a visual comparison of the performance of all tested implementations.


<a href="http://www.javacreed.com/wp-content/uploads/2013/03/List_Performance_1.png" class="preload" rel="prettyphoto" title="List add() Method" ><img src="http://www.javacreed.com/wp-content/uploads/2013/03/List_Performance_1.png" alt="List add() Method" width="481" height="289" class="size-full wp-image-4677" /></a>


The <code>Vector</code> is slightly slower than the others (excluding the <code>CopyOnWriteArrayList</code>).  This is because the <code>Vector</code> is thread-safe and the insert operation made use of locks.  These lock were not contented but nevertheless, synchronisation will always reduce the performance.  With that said, the difference is hardly measured and this is insignificant for most applications.  Should you refactor all your code and replace the <code>Vector</code> with another implementation?  The answer is simple: <strong>NO</strong>.


What we learn from this test is that it is important to know the API and how the implementations we are using behave.  The <code>CopyOnWriteArrayList</code> is expected to perform slower than the others when manipulated, thus unless required, prefer the other <code>List</code> implementations, if the list will be altered (elements are added or removed).  On the other hand, the <code>Iterator</code> (<a href="http://docs.oracle.com/javase/7/docs/api/java/util/Iterator.html" target="_blank">Java Doc</a>) returned by the <code>CopyOnWriteArrayList</code> implementation, does not require any further synchronisation and will not fail if the list is modified.


<h2>Retrieving Elements from a List</h2>

The second test that was performed was the retrieving of the elements from the list at a given index.  The following code fragment shows the operations performed on each list implementation.


<pre>
  private final String pattern = "Element %d";

  @Override
  public long timeAction(final List&lt;String&gt; list, final int limit) {
    for (int i = 0; i < limit; i++) {
      list.add(String.format(pattern, i));
    }

    final long start = System.nanoTime();
    for (int i = 0, size = list.size(); i &lt; limit; i++) {
      list.get(i % size);
    }
    return System.nanoTime() - start;
  }
</pre>


Note that the list implementation was first populated and the population process was not measured.  Only the retrieval operation was measured during this test.


This test uncovered the weakness of the <code>LinkedList</code> as illustrated by the following graph.


<a href="http://www.javacreed.com/wp-content/uploads/2013/03/List_Performance_2.png" class="preload" rel="prettyphoto" title="List get() Method" ><img src="http://www.javacreed.com/wp-content/uploads/2013/03/List_Performance_2.png" alt="List get() Method" width="483" height="289" class="size-full wp-image-4683" /></a>


The time taken for all other lists to execute this test was negligible and their differences are hardly noticeable.  On the other hand, a <code>LinkedList</code> is expected to perform poorly in this test as it has to iterate through all previous elements in order to get to the required one.  While the other lists implementation will take almost no time to access the last element, the <code>LinkedList</code> will have to perform <em>n</em> operations (where <em>n</em> is the index from where the element is retrieved).  If we need to retrieve the 100th elements, then the <code>LinkedList</code> needs to iterate 100 times, before it can reach this element.


If the list will be accessed in a random manner, as tested here, one should avoid the <code>LinkedList</code> implementation as this performs slower than the other implementations.  If this is not possible for various reasons, try to perform the retrieval operations through another list instance as shown in the following example.


<pre>
int index = ...
LinkedList&lt;String&gt; ll = ...
List&lt;String&gt; list = new ArrayList&lt;&gt;(ll);
list.get(index);
</pre>


The example shown above has its limitations too.  While this example would perform well in a <em>convert-once-use-many-times</em> scenario, it will perform poorly when we have to convert the <code>LinkedList</code> several times (such as once for every retrieval operation).


<h2>Iterate through all Elements in a List</h2>


The third test that was conducted iterated through the list content.  As shown in the table above, all implementations performed more or less the same.  One can argue that the <code>LinkedList</code> was the slowest of them all, as shown below.


<a href="http://www.javacreed.com/wp-content/uploads/2013/03/List_Performance_3.png" class="preload" rel="prettyphoto" title="List iterator() Method" ><img src="http://www.javacreed.com/wp-content/uploads/2013/03/List_Performance_3.png" alt="List iterator() Method" width="482" height="289" class="size-full wp-image-4695" /></a>


The difference is not very significant, even though the graph makes this looks worse than it seems.


<h2>Get the list size</h2>

The final test measured the time it takes to retrieve the size of the list as shown in the following figure.


<a href="http://www.javacreed.com/wp-content/uploads/2013/03/List_Performance_4.png" class="preload" rel="prettyphoto" title="List size() Method" ><img src="http://www.javacreed.com/wp-content/uploads/2013/03/List_Performance_4.png" alt="List size() Method" width="482" height="289" class="size-full wp-image-4696" /></a>


Similar to the previous test, most implementation took the same time.


<h2>Conclusion</h2>

In this article we compared various lists implementations and measured their performance when executing some common actions.  Some implementations performed poorly when compared to the others are highlighted in the first two tests.  This does not mean that those that did not do well should never be used.  On the contrary, all implementations have their place and here we only considered single threading environment.  Before using any given list, stop and think how this list is going to be used and run some tests like the ones we saw here before jumping into any hasty conclusions.
