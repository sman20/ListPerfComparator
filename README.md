# ListPerfComparator
List Concurrent Performance Comparator
First release of List Performance Comparator (with command line interface).

The List Concurrent Performance Comparator measures performance of different types of lists -
<code>CopyOnWriteArrList, SynchronizedRandomAccessList, ArrayList, LinkedList</code> -
in the case when the Lists are accessed by two concurrent threads performing <code>get()</code> or <code>add()</code> actions.

<p>The Comparator has got an interactive command-line menu through which it is possible to :</p>
<ul>
    <li>show current configuration on the screen</li>
    <li>update current configuration</li>
    <li>run tests and show average results for lists under tests</li>
    <li>the following list types can be selected for test :
        <ul style="list-style-type: circle;">
            <li><code>CopyOnWriteArrList</code></li>
            <li><code>SynchronizedRandomAccessList</code></li>
            <li><code>ArrayList</code></li>
            <li><code>LinkedList</code></li>
        </ul>
     </li>
</ul>
<h3>Concurrent performance test description</h3>
<h4>Mechanism of retrieving data</h4>
Two <code>Callable</code> threads perform a selected action (<code>access</code> a member or <code>add</code> a member) on first and second halves of a list respectively.
Submitting the threads to <code>newFixedThreadPool</code> and starting them simultaneously via <code>CountDownLatch</code>.
The threads will compete to perform the action on the assigned part of the list.
<br><br>
<h4>Further data processing</h4>
The results time is aggregated separately for each thread of each list under test. Then averages are calculated in <code>ms</code>
and formatted results are printed out.
<br><br><i>Example:</i><br>
<pre>          CopyOnWriteArrayList, size [1000]  -  action: get()  |  thread 1 :       2 ms  |  thread 2 :       2 ms
  SynchronizedRandomAccessList, size [1000]  -  action: get()  |  thread 1 :      11 ms  |  thread 2 :      13 ms</pre>

<h3>JUnit tests</h3>
JUnit tests are covering a part of functionality.
<br><br><br>
 <p>
     @author S.V.
     @version 1.0.0
     @since 2021-05-01
 </p>
