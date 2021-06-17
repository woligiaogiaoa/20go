package com.aaaaa.a2k

import org.junit.Assert.*
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayDeque

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

   /* fun detectCycle(head: ListNode?): ListNode? {
        head ?: return null

    }*/

    @Test
    fun testBQ(){

    }
    // 121 2 12 12 213 424 345 4
   /* fun topKFrequent(nums: IntArray, k: Int): IntArray {

    }*/

    class BigQueue(val size: Int){

        val deque:Deque<Int> =LinkedList<Int>()

        fun poll(`val`: Int) {
            if (!deque.isEmpty() && `val` == deque.peek()) {
                deque.poll()
            }
        }

        fun add(`val`: Int) {
            while (!deque.isEmpty() && `val` > deque.last) {
                deque.removeLast()
            }
            deque.add(`val`)
        }
        //队列队顶元素始终为最大值
        fun peek(): Int {
            return deque.peek()
        }
    }



    fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {

        val queque=BigQueue(nums.size)

        val res= mutableListOf<Int>()

        repeat(nums.size){ index ->
            queque.add(nums[index])
            if(index>k-1){
                queque.poll(nums[index - (k - 1) - 1])
            }
            if(index>=k-1){
                res.add(queque.peek())
            }
        }

        return  res.toIntArray()
    }

    fun evalRPN(tokens: Array<String>): Int {

        val stack=Stack<String>()

        tokens.forEach {
            if(it.equals("+") || it.equals("-") ||it.equals("*") ||it.equals("/") ){
                val a=stack.pop().toInt()
                val b=stack.pop().toInt() // b/a
                if(it.equals("+")){
                    val new=a+b
                    stack.push(new.toString())
                }
                if(it.equals("-")){
                    val new=b-a
                    stack.push(new.toString())
                }
                if(it.equals("*")){
                    val new=a*b
                    stack.push(new.toString())
                }
                if(it.equals("/")){
                    val new=b/a
                    stack.push(new.toString())
                }

            }else{
                stack.push(it)
            }
        }

        return stack.pop().toInt()

    }

    fun removeDuplicates(s: String): String {

        val stack=Stack<Char>()

        s.forEach {
            val emptyBeforePush=stack.isEmpty()
            if(!emptyBeforePush){
                val top=stack.peek()
                if(top.equals(it)){
                    stack.pop()
                }else{
                    stack.push(it)
                }
            }else{
                stack.push(it)
            }
        }

        val builder=StringBuilder()

        val resStack=Stack<Char>()

        while(!stack.isEmpty()){
            val c=stack.pop()
            resStack.push(c)
        }

        while (!resStack.isEmpty()){
            val c=resStack.pop()
            builder.append(c)
        }

        return builder.toString()
    }

    fun isValid(s: String): Boolean {
        val stack=Stack<Char>()
        s.forEach {
            stack.push(it)
            if(it .equals(')')||it .equals('}')||it .equals(']')){
                val target=if( it.equals(')') ) '(' else if (it.equals('}')) '{' else '['
                val new =stack.pop()
                if(stack.isEmpty()) {
                    return false
                }
                val old=stack.pop()
                if(!old.equals(target))
                    return false
            }
        }

        return stack.isEmpty()
    }

    class MyStack() {

        /** Initialize your data structure here. */

        val queue=ArrayDeque<Int>()

        val tempQueue=ArrayDeque<Int>()

        var size=0

        /** Push element x onto stack. */
        fun push(x: Int) {
            queue.addLast(x)
            size++
        }

        /** Removes the element on top of the stack and returns that element. */
        fun pop(): Int {
            if(empty()) return -1

            size--

            var res:Int=-1
            repeat(queue.size){ index->

                val can=queue.removeFirst()

                if(index!=queue.size-1){
                    tempQueue.addLast(can)
                }

                if(index==queue.size-1)
                    res=can
            }

            repeat(tempQueue.size){
                val can1=tempQueue.removeFirst()
                queue.addLast(can1)
            }

            return res
        }

        /** Get the top element. */
        fun top(): Int {
            if(empty()) return -1
            val top=pop()
            queue.addLast(top)
            return  top
        }

        /** Returns whether the stack is empty. */
        fun empty(): Boolean {
            return queue.isEmpty()
        }

    }

    /**
     * Your MyStack object will be instantiated and called as such:
     * var obj = MyStack()
     * obj.push(x)
     * var param_2 = obj.pop()
     * var param_3 = obj.top()
     * var param_4 = obj.empty()
     */


    class MyQueue() {

        /** Initialize your data structure here. */

        var size=0

        val dataStack=Stack<Int>()
        val tempStack=Stack<Int>()

        /** Push element x to the back of queue. */
        fun push(x: Int) {
            dataStack.push(x)
            size++
        }

        /** Removes the element from in front of queue and returns that element. */
        fun pop(): Int {
            if(empty()) return -1
            while (!dataStack.isEmpty()){
                tempStack.push(dataStack.pop())
            }
            val res =tempStack.pop()
            size--
            while (!tempStack.isEmpty()){
                dataStack.push(tempStack.pop())
            }
            return  res
        }

        /** Get the front element. */
        fun peek(): Int {
            if(empty()) return -1
            while (!dataStack.isEmpty()){
                tempStack.push(dataStack.pop())
            }
            val res= tempStack.peek()
            while (!tempStack.isEmpty()){
                dataStack.push(tempStack.pop())
            }
            return  res
        }

        /** Returns whether the queue is empty. */
        fun empty(): Boolean {
            return size<=0
        }

    }

    /**
     * Your MyQueue object will be instantiated and called as such:
     * var obj = MyQueue()
     * obj.push(x)
     * var param_2 = obj.pop()
     * var param_3 = obj.peek()
     * var param_4 = obj.empty()
     */
    fun strStr(haystack: String, needle: String): Int {
        //hello ll
        if(needle.isEmpty()) return 0
        //        end
        if(haystack.length<needle.length) return -1

        val next=IntArray(needle.length)

        fun IntArray.getNext(needle: String){
            this[0]=0
            for(index in 1..needle.length-1){

                //j..index
                var found=false
                for(j in 1..index){
                    if(needle.substring(j, index + 1).equals(needle.substring(0, index - j + 1))){
                        this[index]=index-j+1
                        found=true
                        break
                    }
                }
                if(!found)
                this[index]=0
            }
        }

        next.getNext(needle)

        var pt=0

        var needleIndex=0

        //hello ll

        do{
            if(needle[needleIndex].equals(haystack[pt])){

                if(needleIndex==needle.length-1)
                    return pt-(needle.length-1)
                pt++
                needleIndex++
            } else{ //bupipei
                val nextIndex=if(needleIndex==0) 0 else  next[needleIndex - 1]
                if(nextIndex==0){
                    if(needleIndex==0){
                        pt++
                    }
                    needleIndex=nextIndex
                }else{
                    needleIndex=nextIndex
                }
            }
        }while (pt<=haystack.length-1) //pt valid

        return -1


    }

    fun IntArray.getNext(needle: String){
        this[0]=0
        for(index in 1..needle.length-1){

            //j..index
            var found=false
            for(j in 1..index){
                if(needle.substring(j, index + 1).equals(needle.substring(0, index - j + 1))){
                    this[index]=index-j+1
                    found=true
                    break
                }
            }
            if(!found)
                this[index]=0
        }
    }

    @Test
    fun testKMP(){
        val array= IntArray(5){
            0
        }
        val a1=IntArray(5){
            0
        }.apply {
            getNext("hello")
        }

        repeat(5){ index ->
            assertEquals(array[index], a1[index])
        }
        "hello".indexOf("ll")
        assertEquals(1, 1)
        assertEquals(2, strStr1("hello", "ll"))
    }

    fun strStr1(haystack: String, needle: String): Int {
        if(needle.isEmpty()) return 0
        //        end
        if(haystack.length<needle.length) return -1


        val endIndex=haystack.length-1
        val last=endIndex-(needle.length-1)

        for(i in 0..last){
            println("strStr:${haystack.substring(i, i + needle.length)}")
            if(haystack.substring(i, i + needle.length).equals(needle)){
                return i
            }
        }

        return -1
    }

    @Test
    fun testStr(){
        assertEquals(2, strStr("hello", "ll"))
    }


    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        // 1 2 3 4  4 4 4
        val head=ListNode(1).apply {
            next=ListNode(2).apply {
                next=ListNode(3).apply {
                    next= ListNode(4)
                }
            }
        }
        var newHead: ListNode? =swapPairs1(head)

        val datas= mutableListOf<Int>()

        while(newHead!=null){
            datas.add(newHead.`val`)
            print(datas)
            newHead=newHead.next
        }

        assertEquals("[2,1,4,3]", datas.toString().replace(" ", "", true))

    }

    @Test
    fun testRe(){
        val new =reverseIJ1("qweqwewq", 0, 3)
        assertEquals(new, "qewqwewq")
    }
}

fun removeNthFromEnd(head: ListNode?, n: Int): ListNode? {

    var slow: ListNode? =head
    var fast: ListNode? =head

    var pre: ListNode =ListNode(-1).apply {
        next=head
    }

    repeat(n - 1){
        fast=fast!!.next
    }


    while(fast!!.next!=null){
        pre=pre.next!!
        slow=slow!!.next
        fast= fast!!.next
    }

    if(slow===head){
        return slow!!.next
    }

    pre.next=slow!!.next
   slow.next=null
   return head



}


fun swapPairs1(head: ListNode?): ListNode? {

    head ?: return head

    var first: ListNode? =ListNode(-1)
    var second: ListNode? =ListNode(-1)
    first!!.next=second
    second!!.next=head

    var newHead: ListNode? =null
    var pre: ListNode? =second
    //[1][2][3][4]


    while(second!!.next!=null && second.next!!.next!=null){
        first=second.next
        second=second!!.next!!.next

        val pairTailNext=second!!.next

        val new1=second

       second.next=first
        first!!.next=pairTailNext

        second=first

        first=new1

        pre!!.next=first
        pre=second

        if(newHead==null){
            newHead=first
        }
    }
    return if(newHead==null) head else newHead

}

fun reverseIJ1(s: String, i1: Int, j1: Int):String{
    var s1=s.toCharArray()
    var i =i1
    var j =j1
    while(i<j){

        var jchar=s1[j]
        s1[j]=s1[i]
        s1[i]=jchar

        i++
        j--
    }

    return s1.toString()
}

fun reverseStr1(s: String, k: Int): String {




    var res=s

    var index=0

    while(index<=s.length-1){
        //index...index+k-1

        var end=index+k-1
        var start=index
        if(end>s.length-1) end=s.length-1
        res=reverseIJ1(res, start, end)
        index += 2*k
    }

    return res
}

