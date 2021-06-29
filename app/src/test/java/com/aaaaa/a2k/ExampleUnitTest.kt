package com.aaaaa.a2k

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.math.max

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

   /* fun detectCycle(head: ListNode?): ListNode? {
        head ?: return null

    }*/


    fun lowestCommonAncestor(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode? {

        root?: return null

        if(root===q || root==p)
            return root

        val left=lowestCommonAncestor(root?.left,p,q)
        val right=lowestCommonAncestor(root?.right,p,q)

        if(left!=null && right !=null) return  root

        if(left!=null && right==null){
            return  left
        }else if(left==null && right!=null){
            return  right
        }else{
            return null
        }
    }

    fun lowestCommonAncestorq(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode? {

        fun isAncestor(root: TreeNode?, p: TreeNode?): Boolean {
            root?: return false
            p?: return false
            if(root.left===p || root.right===p) return true
            return isAncestor(root.left,p) || isAncestor(root.right,p)
        }

        root?: return null
        q?: return null
        p?: return null

        var parent:TreeNode?=null
        val queue:Deque<TreeNode> =LinkedList()

        queue.offer(root)
        while(!queue.isEmpty()){
            repeat(queue.size){
                val a =queue.poll()
                if(isAncestor(a,p) && isAncestor(a,q))
                    parent=a
                a.left?.also { queue.offer(it) }
                a.right?.also { queue.offer(it) }
            }
        }
        return parent
    }

    fun findMode(root: TreeNode?): IntArray {

        var pre:Int?=null

        var  count=0
        var  maxCount=0

        val res= mutableListOf<Int>()


        fun traversal(root: TreeNode?){
            root?: return

            traversal(root.left)
            root.also {
                if(pre==null){
                    count++
                }

                else{
                    if(root.`val`==pre){
                        count++
                    }else{
                        count=1
                    }
                }

                if(count>maxCount){
                    res.removeAll { true }
                    res.add(root.`val`)
                    maxCount=count
                } else if(count==maxCount){
                    res.add(root.`val`)
                }

                pre=root.`val`
            }


            traversal(root.right)
        }

        traversal(root)

        return res.toIntArray()
    }

    fun getMinimumDifference(root: TreeNode?): Int {

       var pre:Int?=null

       var min:Int?=null

       fun traversal(root: TreeNode?){
           root?: return

           traversal(root.left)
           root?.also {
               if(pre!=null){
                   if(min==null)
                       min=Math.abs(pre!!-root.`val`)
                   else
                       min=Math.min(min!!,Math.abs(pre!!-root.`val`))
               }
               pre=root.`val`
           }
           traversal(root.right)
       }

       traversal(root)
       return min ?: 0
   }

    fun isValidBST(root: TreeNode?): Boolean {

        var now:Int?=null

        var sheng=true
        //5 1 4 n n 3 6

        fun traversal(root: TreeNode?){
            root ?: return

            traversal(root.left)

            if( now!=null){
                if( root.`val`<=now!!){
                    sheng=false
                }
            }

            now=root.`val`

            traversal(root.right)
        }

        traversal(root)

        return sheng
    }

    fun searchBST(root: TreeNode?, `val`: Int): TreeNode? {
        root ?: return null
        if(root.`val`==`val`) return  root
        if(root.`val`>`val`) return  searchBST(root.left,`val`)
        return   searchBST(root.left,`val`)
    }

    fun constructMaximumBinaryTree(nums: IntArray): TreeNode? {
        val size=nums.size
        if(size<=0) return null
        if(size==1) return TreeNode(nums[0])

        var max=Int.MIN_VALUE
        var iterator=0

        var maxIndex=0
        nums.forEach {
            if(it>max){
                max=it
                maxIndex=iterator
            }
            iterator++
        }

        val root=TreeNode(nums[maxIndex])

        root.left=constructMaximumBinaryTree(
                nums.filterIndexed { index, i ->
                    index<maxIndex
                }.toIntArray()
        )
        root.right=constructMaximumBinaryTree(
                nums.filterIndexed { index, i ->
                    index>maxIndex
                }.toIntArray()
        )
        return root
    }

    fun buildTree(inorder: IntArray, postorder: IntArray): TreeNode? {

        fun traversal(inorder: IntArray, postorder: IntArray):TreeNode?{
            val size=postorder.size
            if(size<=0) return null
            val root=TreeNode(postorder[size-1])

            if(postorder.size==1) return root

            val rootIndexInOrder=inorder.indexOf(root.`val`)

            val inOrderLeft=inorder.filterIndexed { index, i ->
                index<rootIndexInOrder
            }.toIntArray()
            val inOrderRight=inorder.filterIndexed { index, i ->
                index>rootIndexInOrder
            }.toIntArray()

            val postOrderLeft=postorder.filterIndexed { index, i ->
                index<inOrderLeft.size
            }.toIntArray()

            val postOrderRight=postorder.filterIndexed { index, i ->
                inOrderLeft.size<=index && index<postorder.size-1
            }.toIntArray()

            root.left=traversal(inOrderLeft, postOrderLeft)
            root.right=traversal(inOrderRight, postOrderRight)

            return root
        }

        return traversal(inorder,postorder)

    }

    fun hasPathSum(root: TreeNode?, targetSum: Int): Boolean {
        root ?: return false

        fun traversal(root: TreeNode?,sum:Int): Boolean {
            root?: return false
            if(root.left==null && root.right==null){
                if(root.`val`+sum==targetSum){
                    return true
                }
                else{
                    return false
                }
            }
            return  traversal(root.left,root.`val`+sum) || traversal(root.right,root.`val`+sum)
        }

        return traversal(root,0)
    }

    fun findBottomLeftValue(root: TreeNode?): Int   {

        root?: return -1

        var Deep=-1

        var res =root.`val`

        fun traversal(root: TreeNode?,deep:Int){
            root?: return
            if(root.left==null && root.right==null){
                if (deep>Deep){
                    Deep=deep
                    res =root.`val`
                }
            }else{
                root?.left?.also {
                    traversal(it,deep+1)
                }
                root?.right?.also {
                    traversal(it,deep+1)
                }
            }
        }

        traversal(root,0)

        return res
    }

    fun findBottomLeftValue1(root: TreeNode?): Int {
        root ?: return -1
        val quque:Deque<TreeNode> = LinkedList()
        quque.offerFirst(root)


        var node:TreeNode?=null
        while(!quque.isEmpty()){
            repeat(quque.size){ index ->
                val a=quque.poll()

                if(index==0){
                    node=a
                }

                a.left?.also { quque.offer(it) }
                a.right?.also { quque.offer(it) }
            }
        }

        return node!!.`val`
    }

    fun sumOfLeftLeaves1(root: TreeNode?): Int {
        root ?: return 0
        var sum=0

        fun preDo(root: TreeNode?){
            root ?: return
            if(root.left!=null && root.left!!.left==null && root.left!!.right==null){
                sum+=root.left!!.`val`
            }
            preDo(root.left)
            preDo(root.right)
        }

        preDo(root)
        return sum
    }

    fun sumOfLeftLeaves(root: TreeNode?): Int {
        root ?: return 0
        var sum=0
        val stack=Stack<TreeNode>()
        stack.push(root)
        while(!stack.isEmpty()){
            val a =stack.pop()
            a  //do some with a
            if(a.left!=null && a.left!!.left==null && a.left!!.right==null){
                sum+=a.left!!.`val`
            }
            a.left?.also { stack.push(it) }
            a.right?.also { stack.push(it) }

        }

        return  sum

    }


    /**
     * Example:
     * var ti = TreeNode(5)
     * var v = ti.`val`
     * Definition for a binary tree node.
     * class TreeNode(var `val`: Int) {
     *     var left: TreeNode? = null
     *     var right: TreeNode? = null
     * }
     */
    fun mergeTrees1(root1: TreeNode?, root2: TreeNode?): TreeNode? {
        if(root1==null && root2 ==null){
            return null
        }
        var  newVal=0
        root1?.also { newVal+= it .`val` }
        root2?.also { newVal+= it .`val` }
        val new=TreeNode(newVal)
        new.left=mergeTrees1(root1?.left,root2?.left)
        new.right=mergeTrees1(root1?.right,root2?.right)
        return new
    }

    fun mergeTrees(root1: TreeNode?, root2: TreeNode?): TreeNode? {
        root1?: return root2
        root2?: return root1
        val stack=Stack<TreeNode>()
        stack.push(root2)
        stack.push(root1)
        while(!stack.isEmpty()){
            val a =stack.pop()
            val b =stack.pop()
            a.`val`+=b.`val`
            if(a.left!=null && b.left!=null){
                stack.push(b.left)
                stack.push(a.left)
            }else{
                if(a.left==null)
                    a.left=b.left
            }

            if(a.right!=null && b.right!=null){
                stack.push(b.right)
                stack.push(a.right)
            }else{
                if(a.right==null)
                    a.right=b.right
            }
        }

        return root1
    }

    fun binaryTreePaths(root: TreeNode?): List<String> {


        fun consString(node:TreeNode?,path:String,res:MutableList<String>){
            node?: return
            if(node.left==null && node?.right==null){

                val a= if(!path.isEmpty()) path+"->"+node.`val` else  path+node.`val`
                res.add(a)
            }
            node.left?.also {
                val a= if(!path.isEmpty()) path+"->"+node.`val` else  path+node.`val`
                consString(it,a,res )
            }

            node.right?.also {
                val a= if(!path.isEmpty()) path+"->"+node.`val` else  path+node.`val`
                consString(it,a,res )
            }
        }

        val res = mutableListOf<String>()

        consString(root,"",res)

        return res
    }

    fun travelsal1(root: TreeNode?){
        root ?: return
        val stack=Stack<TreeNode>()

        stack.push(root)
    }



    fun countNodes(root: TreeNode?): Int {
        root ?:return  0
        val quque:Deque<TreeNode> = LinkedList()
        quque.offerFirst(root)

        var size=0
        while(!quque.isEmpty()){
            repeat(quque.size){
                val a =quque.poll()
                size++
                a.left?.also {
                    quque.add(it)
                }
                a.right?.also {
                    quque.add(it)
                }
            }
        }

        return size

    }

    fun preOrder(root: TreeNode){
        val stack=Stack<TreeNode>()
    }

    fun isBalanced1(root: TreeNode?): Boolean {
        var root: TreeNode? = root ?: return true
        val stack: Stack<TreeNode> = Stack()
        var pre: TreeNode? = null
        while (root != null || !stack.isEmpty()) {
            while (root != null) {
                stack.push(root)
                root = root.left
            }
            val inNode = stack.peek()
            // 右结点为null或已经遍历过
            if (inNode.right == null || inNode.right === pre) {
                // 比较左右子树的高度差，输出
                if (Math.abs(getHeight(inNode.left) - getHeight(inNode.right)) > 1) {
                    return false
                }
                stack.pop()
                pre = inNode
                root = null // 当前结点下，没有要遍历的结点了
            } else {
                root = inNode.right // 右结点还没遍历，遍历右结点
            }
        }
        return true
        //
    }

    /**
     * 层序遍历，求结点的高度
     */
    fun getHeight(root: TreeNode?): Int {
        if (root == null) {
            return 0
        }
        val deque: Deque<TreeNode?> = LinkedList()
        deque.offer(root)
        var depth = 0
        while (!deque.isEmpty()) {
            val size: Int = deque.size
            depth++
            for (i in 0 until size) {
                val poll = deque.poll()
                if (poll!!.left != null) {
                    deque.offer(poll.left)
                }
                if (poll.right != null) {
                    deque.offer(poll.right)
                }
            }
        }
        return depth
    }


    class TreeNode(var `val`: Int) {
             var left: TreeNode? = null
             var right: TreeNode? = null
    }

    fun height(root: TreeNode?):Int{
        root?: return 0
        return Math.max(height(root.left), height(root.right))+1
    }

    fun isBalanced(root: TreeNode?): Boolean {

        if(height(root)<=2) return true

        return isBalanced(root?.left) && isBalanced(root?.right) &&
               Math.abs(height(root?.left) - height(root?.right)) <2
    }

    fun minDepth(root: TreeNode?):Int{
        root ?: return 0
        if(root.left==null && root.right==null){
            return 1
        }
        if(root.left==null){
            return 1+ minDepth(root.right)
        }
        if(root.right==null){
            return 1+ minDepth(root.left)
        }
        return Math.min(minDepth(root.left),minDepth(root.right))+1
    }


    fun minDepth1(root: TreeNode?): Int {
        //layer travelsal
        val queue= mutableListOf<TreeNode>()
        root ?: return 0
        queue.add(root)
        var depth= 0
        while(!queue.isEmpty()){
            depth++
            repeat(queue.size){
                val node=queue.removeAt(0)
                if(node.left==null && node.right==null)
                    return depth
                node.left?.also { queue.add(it) }
                node.right?.also { queue.add(it) }
            }
        }
        return depth
    }

    fun maxDepth(root: TreeNode?): Int {
            //layer travelsal
        val queue= mutableListOf<TreeNode>()
        root ?: return 0
        queue.add(root)
        var depth= 0
        while(!queue.isEmpty()){
            depth++
            repeat(queue.size){
                val node=queue.removeAt(0)
                node.left?.also { queue.add(it) }
                node.right?.also { queue.add(it) }
            }
        }
        return depth
    }


    fun isSymmetric3(root: TreeNode): Boolean {
        val deque: Queue<TreeNode?> = LinkedList()
        deque.offer(root.left)
        deque.offer(root.right)
        while (!deque.isEmpty()) {
            val leftNode = deque.poll()
            val rightNode = deque.poll()
            if (leftNode == null && rightNode == null) {
                continue
            }

            if (leftNode == null || rightNode == null || leftNode.`val` !== rightNode.`val`) {
                return false
            }

            deque.offer(leftNode.left)
            deque.offer(rightNode.right)
            deque.offer(leftNode.right)
            deque.offer(rightNode.left)
        }
        return true
    }

    //[1,2,2,2,null,2]
    fun isSymmetric(root: TreeNode?): Boolean {
        val queque=LinkedList<TreeNode?>()
        queque.offerFirst(root?.left)
        queque.offerLast(root?.right)
        while(!queque.isEmpty()){
            val a=queque.pollFirst()
            val b=queque.pollLast()
            if(a==null && b==null) continue
            if(a==null || b ==null || a.`val`!=b.`val`) return false
            queque.offerFirst(a.right)
            queque.offerLast(b.left)
            queque.offerFirst(a.left)
            queque.offerLast(b.right)
        }
        return true
    }

    fun invertTree(root: TreeNode?): TreeNode?{

        fun invertSub(root: TreeNode?){
            root?: return
            invertSub(root.left)
            invertSub(root.right)

            val temp=root.left
            root.left=root.right
            root.right=temp
        }

        invertSub(root)

        return  root
    }

    fun invertTree1(root: TreeNode?): TreeNode? {

        fun InOrderDo(node: TreeNode?, action: TreeNode.() -> Unit){
           node?.also {
              it.apply {
                  this.action()
              }
               it.left?.apply {
                   InOrderDo(it.left, action)
               }
               it.right?.apply {
                   InOrderDo(it.right, action)
               }
           }
        }

        InOrderDo(root){
            val temp=right
            right=left
            left=temp
        }
        return root
    }

    @Test
    fun testStackInOrder(){
        val data=TreeNode(1).apply {
            left= TreeNode(2)
            right=TreeNode(3).apply {
                left= TreeNode(4)
                right= TreeNode(5)
            }
        }
        assertArrayEquals(arrayOf(2, 1, 4, 3, 5).toIntArray(), inOrder(TreeNode(1).apply {
            left = TreeNode(2)
            right = TreeNode(3).apply {
                left = TreeNode(4)
                right = TreeNode(5)
            }
        }))
    }


    fun inOrder(root: TreeNode?):IntArray{

        val data= mutableListOf<Int>()
        val st=Stack<TreeNode?>()
        root?.also { st.push(it) }
        while(!st.isEmpty()){
            val top=st.pop()
            if(top!=null){
                top.right?.also { st.push(it) }
                st.push(top)
                st.push(null)
                top.left?.also { st.push(it) }
            }else{
                val targer: TreeNode? =st.pop()
                //do something
                data.add(targer!!.`val`)
            }
        }
        return data.toIntArray()
    }

    fun levelOrder(root: TreeNode?): List<List<Int>> {

        val res = mutableListOf<MutableList<Int>>()
        val queue= mutableListOf<TreeNode>()
        root?.also {
            queue.add(it)
        }
        while(!queue.isEmpty()){

            val layer= mutableListOf<Int>()
            repeat(queue.size){
                queue.first().left?.also {
                    queue.add(it)
                }
                queue.first().right?.also {
                    queue.add(it)
                }

                layer.add(queue.first().`val`)
                queue.removeAt(0)
            }
            res.add(layer)
        }

        return  res
    }


    @Test
    fun testBQ(){

    }
    // 121 2 12 12 213 424 345 4
   /* fun topKFrequent(nums: IntArray, k: Int): IntArray {

    }*/

    fun topKFrequent(nums: IntArray, k: Int): IntArray {

        val fMap= mutableMapOf<Int, Int>()
        nums.forEach {
            val old=fMap.getOrDefault(it, 0)
            fMap[it]=old+1
        }
        val entries: MutableSet<MutableMap.MutableEntry<Int, Int>> = fMap.entries

        val queque=PriorityQueue<MutableMap.MutableEntry<Int, Int>>(){ mutableEntry: MutableMap.MutableEntry<Int, Int>, mutableEntry1: MutableMap.MutableEntry<Int, Int> ->
            mutableEntry.value-mutableEntry1.value
        }
        entries.forEach {
            queque.add(it)
            if(queque.size>k){
                queque.poll()
            }
        }

        val res=IntArray(queque.size)

        var index =0
        queque.forEach {
            res[index++]=it.key
        }

        return  res
    }

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

