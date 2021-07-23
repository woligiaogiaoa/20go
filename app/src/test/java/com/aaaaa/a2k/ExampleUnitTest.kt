package com.aaaaa.a2k

import org.junit.Assert.*
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.sign

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    // 0 1 2 无覆盖 有摄像头 有覆盖
    fun minCameraCover(root: TreeNode?): Int {

        root?: return 0

        var res=0

        fun t(node:TreeNode?) :Int{
            node?: return 2
            val left =t(node.left)
            val right =t(node.right)

            if(left == 2 && right==2)
                return 0

            if(left==0 || right==0) {
                res++
                return 1
            }

            if(left==1 || right==1)
                return 2


            return -1
        }

        val state= t(root)
        if(state==0)
            res++
        return res

    }

    fun maxProfit(prices: IntArray, fee: Int): Int{

        fun max(a:Int,b:Int)=Math.max(a,b)

        /* if(prices.size<=1)
             return 0*/
/*
        dp[0][0]= 0 dp[0][1]= -prices[0]

        fmax(dp[i][0],dp[i][1])

        dp[i][0] - prices[i+1] -> dp[i+1][1]
        dp[i][0]  or  dp[i][1] + prices[i+1]-fee    -> dp[i+1][0]*/

        //-----------------diver------------

        //return max value at   : day i  State:state
        /* fun dp1(i:Int,state:Int): Int { //i :dayIndex
             if(i==0){
               return if(state==0) 0 else -prices[0]
             }
             else{
                 if(state==1){
                     //1.buy at last day
                     val v1 =dp1(i-1,1)
                     val v2= dp1(i-1,0)-prices[i]
                     return Math.max(v1,v2)
                 }else{ //state=0
                     val v1 =dp1(i-1 ,0)
                     val v2=dp1(i-1,1)+prices[i]-fee
                     return Math.max(v1,v2)
                 }
             }
         }*/


        //res:dp[i][0]
        val dp= mutableListOf<IntArray>().apply {
            repeat(prices.size){
                IntArray(2).also {
                    add(it)
                }
            }
        }

        dp[0][0]=0
        dp[0][1]=-prices[0]
        for(i in 1..prices.size-1){
            dp[i][0]=max(dp[i-1][0],dp[i-1][1]+prices[i]-fee)
            dp[i][1]=max(dp[i-1][1],dp[i-1][0]-prices[i])
        }

        return dp[prices.size-1][0]

    }


    fun maxProfit2(prices: IntArray, fee: Int): Int  {
        var res =0
        var buy=prices[0]+fee
        for(i in 1..prices.size-1){
            if(prices[i]+fee<buy){
                buy=prices[i]+fee

            } else if(prices[i]>buy){
                res+=prices[i]-buy
                buy=prices[i]
            }
        }
        return  res

    }


    /*class Solution {
public:
int maxProfit(vector<int>& prices, int fee) {
    int result = 0;
    int minPrice = prices[0]; // 记录最低价格
    for (int i = 1; i < prices.size(); i++) {
        // 情况二：相当于买入
        if (prices[i] < minPrice) minPrice = prices[i];

        // 情况三：保持原有状态（因为此时买则不便宜，卖则亏本）
        if (prices[i] >= minPrice && prices[i] <= minPrice + fee) {
            continue;
        }

        // 计算利润，可能有多次计算利润，最后一次计算利润才是真正意义的卖出
        if (prices[i] > minPrice + fee) {
            result += prices[i] - minPrice - fee;
            minPrice = prices[i] - fee; // 情况一，这一步很关键
        }
    }
    return result;
}
};*/        fun maxProfit1(prices: IntArray, fee: Int): Int  {
        //found increase blocks

        if (prices.size <= 1) {
            return 0
        }

        var res = 0

        var left: Int? = null
        var leftFound = false

        var max: Int? = null

        for (i in 0..prices.size - 1) {

            if (left == null) {
                left = prices[i]
            } else {

                if (!leftFound) {  //try find a  lowest
                    if (prices[i] <= left) {
                        left = prices[i]
                        //leftFound=false
                    } else {
                        max = prices[i]
                        leftFound = true
                    }
                } else {
                    if (prices[i] >= max!!) {
                        //max not found
                        max = prices[i]
                    } else {
                        //max found  jiesuan
                        val profit: Int = (max as Int) - left as Int
                        if (profit > fee) {
                            res += profit - fee
                        }

                        left = prices[i]
                        leftFound = false
                        max = null
                    }
                }

            }
        }

        if (max != null) {
            val profit: Int = (max as Int) - left as Int
            if (profit > fee) {
                res += profit - fee
            }
        }
        return res

    }

    fun monotoneIncreasingDigits(n: Int): Int {
        /* while(n%10>0){
             val tar=n%10
             //do something eith tar
             n/=10
         }*/

        val data: CharArray = n.toString().toCharArray()

        for (i in 0..data.size - 1) {
            if (i + 1 <= data.size - 1 && data[i] > data[i + 1]) {

                var newT = i
                for (x in i - 1 downTo 0) {
                    if (data[x] == data[i]) {
                        newT = x
                    } else {
                        break
                    }

                }

                data[newT] = data[i] - 1
                for (j in newT + 1..data.size - 1) {
                    data[j] = '9'
                }
                break
            }
        }

        return String(data).toInt()

    }


    fun merge(intervals: Array<IntArray>): Array<IntArray> {
        intervals.sortWith(object : Comparator<IntArray> {
            override fun compare(o1: IntArray, o2: IntArray): Int {
                val delta = o1[0] - o2[0]
                return if (delta > 0) 1 else if (delta < 0) -1 else {
                    val delta1 = o1[1] - o2[1]
                    if (delta > 0) -1 else if (delta == 0) 0 else 1
                }
            }
        })
        if (intervals.isEmpty()) return emptyArray()

        val res = mutableListOf<IntArray>()
        var left = intervals[0][0]
        var right = intervals[0][1]
        for (i in 0..intervals.size - 1) {
            if (intervals[i][0] <= right) {
                right = Math.max(right, intervals[i][1])
            } else {
                res.add(IntArray(2) {
                    if (it == 0) left else right
                })
                left = intervals[i][0]
                right = intervals[i][1]
            }
        }
        res.add(IntArray(2) {
            if (it == 0) left else right
        })

        return res.toTypedArray()
    }

    fun merge1(intervals: Array<IntArray>): Array<IntArray> {
        //递归
        intervals.sortWith(object : Comparator<IntArray> {
            override fun compare(o1: IntArray, o2: IntArray): Int {
                val delta = o1[1] - o2[1]
                return if (delta > 0) 1 else if (delta == 0) 0 else -1
            }
        })
        if (intervals.isEmpty()) return emptyArray()

        val res = mutableListOf<IntArray>().apply {
            add(intervals.get(1))
        }
        for (i in 1..intervals.size - 1) {
            if (intervals[i][0] <= res[res.size - 1][1]) {
                val new = IntArray(2) {
                    if (it == 0) Math.min(
                        res[res.size - 1][0],
                        intervals[i][0]
                    ) else intervals[i][1]
                }
            } else {
                res.add(intervals[i])
            }
        }

        return emptyArray()
    }


    fun partitionLabels(s: String): List<Int> {
        val record = IntArray(26) { -1 }
        repeat(s.length) { index ->
            val target = (s[index] - 97).toInt()
            record[target] = Math.max(record[target], index)
        }

        val res = mutableListOf<Int>()
        var start = 0
        var max = 0
        for (i in 0..s.length - 1) {
            val farestI = (s[i] - 97).toInt().let {
                record[it]
            }
            max = Math.max(max, farestI)
            if (max == i) {
                res.add(i - start + 1)

                //state transition
                start = i + 1
                max = i + 1
            }
        }

        return res

    }

    fun partitionLabels1(s: String): List<Int> {


        var index = 0

        val res = mutableListOf<Int>()

        fun t() {
            val pair = s.indexOfLast {
                it == (s[index])
            }

            if (pair == index) {
                //index..index step end
                index++
                res.add(1)
            } else {

                var max = pair
                for (i in index + 1..pair - 1) {
                    val can = s.indexOfLast { it == s[i] }
                    max = Math.max(max, can)
                }
                res.add(max - index + 1)
                index = max + 1

                //index..max
            }

        }

        while (index <= s.length - 1) {
            t()
        }

        return res
    }

    fun eraseOverlapIntervals(intervals: Array<IntArray>): Int {
        intervals.sortWith(object : Comparator<IntArray> {
            override fun compare(o1: IntArray, o2: IntArray): Int {
                val end1 = o1[1]
                val end2 = o2[1]
                return if (end1 > end2) 1 else if (end1 == end2) 0 else -1
            }
        })

        var end = intervals[0][1]

        var count = 0

        for (i in 1..intervals.size - 1) {
            if (intervals[i][0] < end) {
                count++
            } else {
                end = intervals[i][1]
            }
        }
        return count
    }

    // [[10,16],[2,8],[1,6],[7,12]]
    fun findMinArrowShots(points: Array<IntArray>): Int {


        if (points.isEmpty()) return 0
        points.sortWith(object : Comparator<IntArray> {
            override fun compare(o1: IntArray, o2: IntArray): Int {

                return if (o1[0] < o2[0]) -1 else if (o1[0] == o2[0]) 0 else 1
            }
        })

        var res = 1

        for (i in 1..points.size - 1) {
            if (points[i][0] > points[i - 1][1])
                res++
            else {
                points[i][1] = Math.min(points[i - 1][1], points[i][1])
            }
        }

        return res

    }


    //[5,5,5,10,20]
    //20 10 5 5 5
    fun reconstructQueue(people: Array<IntArray>): Array<IntArray> {

        people.sortWith(object : Comparator<IntArray> {
            override fun compare(p1: IntArray, p2: IntArray): Int {
                val height = p1[0] - p2[0]
                val order = p1[1] - p2[1]

                return if (height == 0) {
                    order
                } else {
                    -1 * height
                }
            }
        })


        val res = mutableListOf<IntArray>()

        for (i in 0..people.size - 1) {
            val p = people[i]
            res.add(p[1], p)
        }

        return res.toTypedArray()

    }

    // 5 15= 5+ 5+ 5  ||  10 +5

    //[5,5,5,10,20]
    fun lemonadeChange(bills: IntArray): Boolean {

        var five = 0
        var ten = 0
        var twenty = 0
        for (i in 0..bills.size - 1) {
            if (bills[i] == 5) {
                five++
                continue
            } else if (bills[i] == 10) {
                if (five > 0) {
                    five--
                    ten++
                    continue
                } else {
                    return false
                }

            } else { //20
                if (five >= 3 || (ten >= 1 && five >= 1)) {
                    if ((ten >= 1 && five >= 1)) {
                        ten--
                        five--
                        twenty++
                        continue
                    } else {
                        five -= 3
                        twenty++
                    }
                } else {
                    return false
                }
            }
        }
        return true
    }


    fun candy1(ratings: IntArray): Int {
        val res = IntArray(ratings.size) {
            1
        }
        /*     for (i in1 )*/
        for (i in 1..ratings.size - 1) {
            if (ratings[i] <= ratings[i - 1]) {
                res[i] = 1
                if (ratings[i] < ratings[i - 1] && res[i - 1] == 1) {
                    //res[i-1]=2
                    for (j in i - 1 downTo 0) {
                        if (ratings[j] > ratings[j + 1] && res[j] <= res[j + 1]) {
                            res[j] = res[j + 1] + 1
                        }
                    }
                }
            } else {
                res[i] = res[i - 1] + 1
            }
        }

        var sum = 0
        res.forEach {
            sum += it
        }
        return sum
    }

    //gas = [1,2,3,4,5] cost = [3,4,5,1,2]
    fun canCompleteCircuit(gas: IntArray, cost: IntArray): Int {

        var sum = 0
        for (i in 0..gas.size - 1) {
            val rest = gas[i] - cost[i]
            sum += rest
        }

        val fakeGas = IntArray(gas.size * 2) {
            gas[it % gas.size]
        }

        val fakeCost = IntArray(gas.size * 2) {
            cost[it % gas.size]
        }

        if (sum < 0)
            return -1

        var count = 0

        var timeCounter = 0

        var i = 0

        while (i <= gas.size + gas.size - 2) {
            count += fakeGas[i] - fakeCost[i]
            if (count < 0) {
                i++
                count = 0
                timeCounter = 0
                continue
            }
            timeCounter++
            if (timeCounter >= gas.size)
                return i - (gas.size - 1)
            i++
        }

        return -1
    }


    fun teseasdsa() {

    }



    //gas = [1,2,3,4,5] cost = [3,4,5,1,2]
    /*fun canCompleteCircuit1(gas: IntArray, cost: IntArray): Int //{

        for(start in 0..gas.size-1){

            var have=0
            var nowAt=start
            while(true){
                val positive=have+gas[nowAt]-cost[nowAt]
                if(positive>=0){
                    have+=gas[nowAt]
                    have-=cost[nowAt]
                    nowAt=if(nowAt==gas.size-1) 0 else nowAt+1

                    if(nowAt==start)
                        return  start
                }
                else{
                    break
                }
            }

        }

        return -1
    }*/


    fun largestSumAfterKNegations(nums: IntArray, k: Int): Int {

        repeat(k){
            nums.sort()
            nums[0]=-1* nums[0]
        }

        var sum=0
        for(i in 0..nums.size-1){
            sum+=nums[i]
        }
        return sum
    }

    fun jump(nums: IntArray): Int {
        if(nums.size<=1)
            return 0
        var far=0

        var count=0

        while(true){
            if(far+nums[far] >= nums.size-1) {
                count++
                return count
            }
            var max=-1

            var next=far

            for(i in far+1..far+nums[far]){ //i
                val fari= i+nums[i]
                if(fari>max){
                    next=i
                    max=fari
                }
            }
            far =next
            count++
        }

    }

    fun canJump(nums: IntArray): Boolean {

        var cover=0
        var index=0
        cover=nums[index]

        var covered= 0

        while(cover<nums.size-1){
            var max=cover
            for(i in cover downTo 0){
                if(i>covered){
                    val eachMax=i+nums[i]
                    max=Math.max(max,eachMax)
                }
            }
            if(max<=cover && cover<nums.size-1)
                return false

            covered=cover
            cover=max

        }

        return true

    }

    fun maxProfit(prices: IntArray): Int {
        var res=0
        for (i in 1..prices.size-1){

            val pro=prices[i]-prices[i-1]
            if(pro>0)
                res+= pro
        }

        return  res
    }


    //1232132312321313213213321321333123123321
    fun maxSubArray(nums: IntArray): Int {

        var count=0
        var res =Int.MIN_VALUE

        for( i in 0..nums.size-1){
            count+= nums[i]
            res=Math.max(count,res)
            if(count<0)
                count= 0
        }
        return  res
        /*var count=0

        var max=Int.MIN_VALUE
        var i=0

        while (i<=nums.size-1){
            count+=nums[i]
            if(count<0){
                i++
                max=Math.max(max,count)
                count=0
                continue
            }
            max=Math.max(max,count)
            i++
        }
        return max*/
    }

    fun wiggleMaxLength(nums: IntArray): Int {
        if(nums.size<=1) return nums.size

        var pre=0

        var count=1

        for( i in 1..nums.size-1){
            val cur=  nums[i]-nums[i-1]
            if( (cur >0 && pre<=0) || (cur <0 && pre>=0)  ){
                count++
                pre=cur
            }
        }
        return  count
    }

    fun findContentChildren(g: IntArray, s: IntArray): Int {
        g.sort()
        s.sort()
        var i=0
        var j=0

        var num=0

        while(j <=s.size-1 && i<=g.size-1){

            val child=g[i]

            while(s[j]<child){
                j++
                if(j>s.size-1)
                    break
            }

            //run out of j
            if(j>s.size-1)
                break

            num++
            j++
            i++
        }

        return num
    }



    fun solveSudoku(board: Array<CharArray>):Unit {

        fun geziInvalid(i:Int, j:Int, char:Char): Boolean {

            val startI=i/3 *3
            val startJ=j/3 * 3
            for(x in startI..startI+2){
                for(y in startJ..startJ+2){
                    if(board[x][y].equals(char))
                        return false
                }
            }
            return true

        }




        fun t():Boolean {

            for(i in 0..board.size-1){
                for(j in 0..board.size-1){
                    if(!board[i][j].equals('.')){
                        continue
                    }else{

                        for(can in '1'..'9'){
                            val added=can
                            if(board[i].contains(added))
                                continue
                            var columnFound=false

                            for(row in 0..8){
                                if(board[row][j]==(added))
                                    columnFound=true
                            }
                            if(columnFound)
                                continue

                            if(!geziInvalid(i,j,added))
                                continue

                            //valid

                            board[i][j]=added
                            if(t()) return true
                            board[i][j]='.'
                        }

                        return false
                    }
                }
            }

            return true
        }

        t()
    }



    fun solveSudoku1(board: Array<CharArray>): Unit {

        //0..8
        fun t(i:Int,j:Int): Boolean {

            fun geziInvalid(i:Int, j:Int, value:Char): Boolean {
                val startI=i/3 *3
                val startJ=i/3 * 3
                for(x in startI..startI+2){
                    for(y in startJ..startJ+2){
                        if(board[x][y].equals(value))
                            return false
                    }
                }
                return true

            }

            if(i==8 && j==8){
                return true
            }

            val nextI= if(j==8) i+1 else i
            val nextJ= if(j==8) j+1 else 0
            val nextValue=board[nextI][nextJ]
            if(!nextValue.equals(".")){
                t(nextI,nextJ)
            } else{ //要填写了
                for(num in 1..9){
                    val added=(num+48).toChar()
                    if(board[nextI].contains(added))
                        continue
                    var columnFound=false
                    for(row in 0..8){
                        if(board[row][nextJ].equals(added))
                            columnFound=true
                    }
                    if(columnFound)
                        continue
                    if(!geziInvalid(nextI,nextJ,added))
                        continue

                    board[nextI][nextJ]=added
                    if(t(nextI,nextJ)){
                        return true
                    }
                    board[nextI][nextI]='.'

                }
            }

            return true

        }

        t(0,-1)
    }



    fun solveNQueens(n: Int): List<List<String>> {
                val paths= mutableListOf<MutableList<Int>>().apply {
                    repeat(n){
                        val row=ArrayList<Int>().apply {
                            repeat(n){
                                add(-1)
                            }
                        }
                        add(row)
                    }
                }

                val res= mutableListOf<MutableList<String>>()


                val pathdata= mutableListOf<String>()

                fun t(row:Int){ // 12345 when n ==5

                    if(row==n-1){

                            res.add(ArrayList(pathdata))

                        return
                    }

                    val  i=row+1 // row index

                    for(j in 0..n-1){
                            // [i][j]
                        var foundIlleague=false
                        for(x in 0..i-1){
                            //every row index x
                            var  targetj =-1
                            paths[x].filterIndexed { index, num ->
                                if(num==1){
                                    targetj=index
                                }
                                true
                            }


                                if(targetj==j || Math.abs(j-targetj)==Math.abs(i -x)){
                                foundIlleague=true
                            }
                        }

                        if(foundIlleague)
                            continue

                        paths[i][j]=1

                        pathdata.add(StringBuilder().apply{
                            repeat(n){ nIndex->
                                append(if(nIndex==j) "Q" else ".")
                                }
                            }.toString())

                        t(row+1)
                        paths[i][j]=-1
                        pathdata.removeAt(pathdata.size-1)
                    }

                }

                t(-1)

                return res
            }



    fun findItinerary1(tickets: List<List<String>>): List<String> {

        if(tickets.isEmpty()){
            return emptyList()
        }
        // JKF qidian >=1
        var ite =0

        val jkfIndexes= mutableListOf<Int>()


        val path= mutableListOf<Int>() //indexes


        var  res = mutableListOf<String>()


        fun t(indexa:Int){

            if(path.size==tickets.size){

                //println(path.toString())

                val can = mutableListOf<String>()


                path.forEach {
                    val ticket: List<String> =tickets[it]
                    if(can.isEmpty()){
                        can.addAll(ticket)
                    }else{
                        can.removeAt(can.size-1)
                        can.addAll(ticket)
                    }
                }

                //println(can.toString())

                if(res.isEmpty()){
                    res.addAll(can)
                    return
                }

                val old=res.run {

                    var ret=""
                    forEach { string ->
                        ret+=string
                    }

                    ret
                }
                val new=can.run {

                    var ret=""
                    forEach { string ->
                        ret+=string
                    }

                    ret
                }

                if(old.compareTo(new)>0){
                    res=can
                }

                return

            }

            for(i in 0..tickets.size-1){

                if(path.isEmpty() &&  tickets[i][0].equals("JFK"))
                    continue

                if(path.contains(i))
                    continue

                val candidates =tickets[i].first()

                if(!path.isEmpty() && !candidates.equals(tickets[path[path.size-1] ] [1]))
                    continue

                path.add(i)
                t(i)

                path.removeAt(path.size-1)
            }

        }

        t(-1)

        return res


    }

    @Test
    fun testContains(){
        val path= mutableListOf<Int>().apply {
            add(123)
            add(123)
            add(123)
            add(123)
        }

        val x =ArrayList<Int>().apply {
            addAll(emptyList())
        }

        val a :Int=123
        assertEquals(true,path.contains(a))

        val t1 = mutableListOf("JFK","SFO")
        val t2 = mutableListOf("JFK","ATL")
        val t3 = mutableListOf("SFO","ATL")
        val t4 = mutableListOf("ATL","JFK")
        val t5 = mutableListOf("ATL","SFO")

        val ts= mutableListOf<MutableList<String>>().apply {
            add(t1)
            add(t2)
            add(t3)
            add(t4)
            add(t5)
        }
        findItinerary(ts)
        assertEquals(true,true)


    }

    fun findItinerary(tickets: List<List<String>>): List<String>{

        if(tickets.isEmpty()) return emptyList()

        val res = mutableListOf<String>().apply {
            add("JFK")
        }

        //remaining
        val m1= HashMap<String,TreeMap<String,Int>>()

        tickets.forEach {
            val temp: TreeMap<String, Int> =m1[it[0]] ?: TreeMap()
            val old=temp[it[1]] ?: 0
            temp.put(it[1],old+1)
            //map[it[0]]=temp
            m1.put(it[0],temp)
        }

        fun t():Boolean{
            if(res.size==tickets.size+1){
                return true
            }
            val nowAt=res.last()
            val next: TreeMap<String, Int>? =m1[nowAt]
            next?: return false
            next.forEach { candidate: Map.Entry<String, Int> ->
                if(candidate.value>0){
                    next.put(candidate.key,candidate.value-1)
                    m1.put(res.last(),next)
                    res.add(candidate.key)
                    if(t()) return true
                    val old: TreeMap<String, Int>? =m1[nowAt]
                    old!!.put(candidate.key,old[candidate.key]!!+1)
                    m1.put(nowAt,old)
                    res.removeAt(res.size-1)
                }
            }

            return false
        }
        t()
        return  res
    }





    fun permuteUnique(nums: IntArray): List<List<Int>> {
            //nums.sort()

            if (nums.isEmpty()) return mutableListOf()

            val path = mutableListOf<Int>()

            val data = mutableListOf<Int>()

            val res = mutableListOf<MutableList<Int>>()

            //val datas: java.util.ArrayList<Int> =ArrayList(nums.toList())

            fun t(index: Int) {

                if(path.size==nums.size){
                    res.add(ArrayList(data))
                    return
                }

                val used = mutableListOf<Int>()

                for (i in 0..nums.size - 1) {
                    if (path.contains(i))
                        continue
                    if (used.contains(nums[i]))
                        continue
                    used.add(nums[i])
                    path.add(i)
                    data.add(nums[i])
                    t(i)
                    path.removeAt(path.size - 1)
                    data.removeAt(data.size - 1)
                }
            }

            t(-1)


            return res
        }




    fun permute(nums: IntArray): List<List<Int>> {

        val path = mutableListOf<Int>() //indexes

        val order = mutableListOf<Int>()

        val res = mutableListOf<MutableList<Int>>()

        if(nums.isEmpty()) return mutableListOf()

        fun t(index: Int) { //node

            if (path.size == nums.size) {
                res.add(ArrayList(order))
                return
            }

            for (i in 0..nums.size - 1) {
                if (path.contains(i))
                    continue

                path.add(i)
                order.add(nums[i])
                t(i)
                order.removeAt(order.size - 1)
                path.removeAt(path.size - 1)
            }
        }

        t(-1)


        return res

    }




    /*输入
[4,6,7,7]
输出
[[4,6],[4,6,7],[4,6,7,7],[4,6,7],[4,7],[4,7,7],[4,7],[6,7],[6,7,7],[6,7],[7,7]]
预期结果
[[4,6],[4,6,7],[4,6,7,7],[4,7],[4,7,7],[6,7],[6,7,7],[7,7]]*/
    fun findSubsequences(nums: IntArray): List<List<Int>> {

        if(nums.size<2) return emptyList()

        val path= mutableListOf<Int>() //datas

        val res = mutableListOf<MutableList<Int>>()

        fun t(index:Int){
            val set = mutableSetOf<Int>()
            for(i in index+1..nums.size-1){

                if(set.contains(nums[i]))
                    continue
                set.add(nums[i])

                val last=if(path.size==0)   Int.MIN_VALUE else path[path.size-1]

                if(nums[i]<last)
                    continue
                path.add(nums[i])
                 //do something
                if(path.size>=2){
                    res.add(ArrayList(path))
                }
                t(i)

                path.removeAt(path.size-1)
            }
        }

        t(-1)

        return  res
    }

    // 1 2 2 2 3 5 6 8 9 10
    fun subsetsWithDup(nums: IntArray): List<List<Int>> {
        nums.sort()
        val path= mutableListOf<Int>()

        val res= mutableListOf<MutableList<Int>>()

        fun dfs(index:Int){
            for(i in index+1..nums.size-1){
                if(i>index+1 && nums[i]==nums[i-1])
                    continue
                path.add(nums[i])
                //do path
                res.add(ArrayList(path))
                dfs(i)
                path.removeAt(path.size-1)
            }
        }

        dfs(-1)
        res.add(mutableListOf())

        return  res
    }



    //1 2 3  7 8 9
    fun subsets(nums: IntArray): List<List<Int>> {
        nums.sort()

        val path= mutableListOf<Int>()

        val res= mutableListOf<MutableList<Int>>()

        fun t(index:Int){

            for(i in index+1..nums.size-1){
                path.add(nums[i])
                res.add(ArrayList(path))
                t(i)
                path.removeAt(path.size-1)
            }
        }

        t(-1)

        res.add(mutableListOf())
        return res
    }

    fun restoreIpAddresses(s: String): List<String> {

        val stringPath= mutableListOf<String>()

        val res = mutableListOf<String>()

        if(s.length<4) return emptyList()

        fun t(index:Int){
            if(stringPath.size==4 && index==s.length-1){
                val data=java.lang.StringBuilder()

                var times=0
                stringPath.forEach {

                    data.append(it)
                    if(times!=3){
                        data.append(".")
                    }
                    times++
                }
                res.add(data.toString())
                return
            }

            for(i in index+1..s.length-1){
                if(stringPath.size==4)
                    break
                val add =s.substring(index+1,i+1 )

                if(add.startsWith("0") && add.length>1){
                    break
                }
                if(add.toString().toInt()>255)
                    break

                stringPath.add(add)
                t(i)
                stringPath.removeAt(stringPath.size-1)
            }
        }
        t(-1)
        return res
    }

    fun huiwen(s:String): Boolean {
        if(s.isEmpty()) return true
        val chars=s.toCharArray()
        var i =0
        var j=s.length-1
        while(i<j){
            if(!s[i].equals(s[j]))
                return false
            i++
            j--
        }
        return true
    }

    fun partition(s: String): List<List<String>> {

        val path= mutableListOf<Int>().apply {}  //end indexes

        val res = mutableListOf<MutableList<String>>()

        val stringPath= mutableListOf<String>()


        if(s.isEmpty()) return emptyList()


        fun t(index:Int){ // end Index of a string (several)

            if(index==s.length-1){
                res.add(ArrayList(stringPath))
            }


            for(i in index+1..s.length-1){
                //i :next end index
                val end=i
                val lastInPath= if(path.size>0) path[path.size-1] else -1

                val pending=s.substring(lastInPath+1,end+1)
                if(!huiwen(pending)){
                    continue
                }

                stringPath.add(pending)
                path.add(i)
                t(i)
                path.removeAt(path.size-1)
                stringPath.removeAt(stringPath.size-1)
            }
        }

        t(-1)
        return res


    }

    //3333334444455556789
    fun combinationSum2(candidates: IntArray, target: Int): List<List<Int>> {
        candidates.sort()
        val path= mutableListOf<Int>()

        var sum =0

        val resSet= hashSetOf<MutableList<Int>>()

        fun t(index:Int){
            if(sum==target){
                resSet.add(ArrayList(path))
                return
            }

            if(sum>target)
                return

            for(i in index+1..candidates.size-1){
                if(sum+candidates[i]>target) break
                if(i>index+1 && candidates[i]==candidates[i-1])
                    continue
                path.add(candidates[i])
                sum +=candidates[i]
                t(i)
                sum-= candidates[i]
                path.removeAt(path.size-1)
            }
        }

        t(-1)

        return resSet.toList()
    }

    fun combinationSum(candidates: IntArray, target: Int): List<List<Int>> {

        candidates.sort()

        var sum=0

        val path= mutableListOf<Int>()

        val res = mutableListOf<MutableList<Int>>()

        fun t(index:Int){
            if(sum==target){
                res.add(ArrayList(path))
                return
            }

            if(sum>target)
                return

            for(i in index..candidates.size-1){
                if(sum+candidates[i]>target)  break
                path.add(candidates[i])
                sum +=candidates[i]
                t(i)
                sum -= candidates[i]
                path.removeAt(path.size-1)
            }
        }

        t(0)

        return res

    }




    /*输入：
"999"
输出：
["www","wwx","wwy","wwz","wxw","xwx","wxy","wxz","wyw","ywx","ywy","wyz","wzw","zwx","zwy","zwz","xww","xwx","wxy","wxz","xxw","xxx","xxy","xxz","xyw","xyx","yxy","xyz","xzw","xzx","zxy","zxz","yww","ywx","ywy","wyz","yxw","yxx","yxy","xyz","yyw","yyx","yyy","yyz","yzw","yzx","yzy","zyz","zww","zwx","zwy","zwz","zxw","zxx","zxy","zxz","zyw","zyx","zyy","zyz","zzw","zzx","zzy","zzz"]
预期结果：
["www","wwx","wwy","wwz","wxw","wxx","wxy","wxz","wyw","wyx","wyy","wyz","wzw","wzx","wzy","wzz","xww","xwx","xwy","xwz","xxw","xxx","xxy","xxz","xyw","xyx","xyy","xyz","xzw","xzx","xzy","xzz","yww","ywx","ywy","ywz","yxw","yxx","yxy","yxz","yyw","yyx","yyy","yyz","yzw","yzx","yzy","yzz","zww","zwx","zwy","zwz","zxw","zxx","zxy","zxz","zyw","zyx","zyy","zyz","zzw","zzx","zzy","zzz"]*/
    fun letterCombinations(digits: String): List<String> {
        val letterMap= arrayOf(
                "", // 0
                "", // 1
                "abc", // 2
                "def", // 3
                "ghi", // 4
                "jkl", // 5
                "mno", // 6
                "pqrs", // 7
                "tuv", // 8
                "wxyz" // 9
       )

        val path= mutableListOf<Char>()

        val res = mutableListOf<MutableList<Char>>()

        if(digits.isEmpty()) return emptyList()

        fun t(index:Int){
            if(index==digits.length-1){
                res.add(ArrayList(path))
                return
            }

            if(index==-1){
                letterMap[digits[index+1].toString().toInt()].forEach { next ->
                    path.add(next)
                    t(index+1)
                    path.removeAt(path.size-1)
                }

                return
            }

            letterMap[digits[index+1].toString().toInt()].forEach {  next ->
                path.add(next)
                t(index+1)
                path.remove(next)
            }
        }

        t(-1)
        return mutableListOf<String>().apply {
            res.forEach { charList: MutableList<Char> ->
                var string=""
                charList.forEach {
                    string +=it
                }

                add(string)
            }
        }
    }

    @Test
    fun asc(){
        val a =97
        assertEquals('a',a.toChar())
    }

        fun combinationSum3(k: Int, n: Int): List<List<Int>> {

           // return  combine(9,k,n)

            val res= mutableListOf<MutableList<Int>> ()

            val path= mutableListOf<Int>()

            var sum = 0

            fun t(node:Int){
                if(sum==n && path.size==k){
                    res.add(ArrayList(path))
                    return
                }
                val need=k-path.size

                val end=9-((k-path.size)-1)

                for(i in node+1..end){
                    path.add(i)
                    sum+=i
                    t(i)
                    path.remove(i)
                    sum-=sum
                }
            }

            t(0)
            return  res
        }


        fun combine(n: Int, k: Int,sum:Int): List<List<Int>> {

            val path= mutableListOf<Int>()

            val res= mutableListOf<MutableList<Int>>()

            fun t(n:Int,k:Int,i:Int){
                if(path.size==k){
                    var pre= 0
                    path.forEach {
                        pre+=it
                    }
                    if(pre==sum)
                        res.add(ArrayList(path))
                    return
                }

                for(j in i+1..n){
                    val need=k-path.size
                    val remainMax=n-j+1
                    if(need>remainMax)
                        return
                    path.add(j)
                    println("add ${j}")
                    t(n,k,j)
                    println("remove ${j}")
                    path.remove(j)
                }
            }

            t(n,k ,0)

            return  res
        }

    @Test
    fun t1212(){
        //combine(4,2)


        for(i in 100..9){
            println(i)
        }
        assertEquals(true,true)
    }

    fun h (root: TreeNode?): Int {
        root ?: return 0
        return  1 + max(h(root.left),h(root.right))
    }

    fun convertBST(root: TreeNode?): TreeNode? {
        var ac=0

        fun t(root: TreeNode?){
            root ?: return
            t (root.right)
            root.`val`+= ac
            ac = root.`val`
            t(root.left)
        }
        t(root)
        return  root
    }


    fun sortedArrayToBST(nums: IntArray): TreeNode? {

        fun consToTree(nums: IntArray):TreeNode?{
            if(nums.size==0 )
                return null

            val medium=(nums.size-1)/2
            val new=TreeNode(nums[medium])

            val left= mutableListOf<Int>()
            for(i in 0..medium-1){
                left.add(nums[i])
            }
            new.left=consToTree(left.toIntArray())

            val right= mutableListOf<Int>()
            for(i in medium+1..nums.size-1){
                right.add(nums[i])
            }
            new.left=consToTree(left.toIntArray())
            new.right=consToTree(right.toIntArray())
            return new
        }

        return  consToTree(nums)

    }

    fun trimBST(root: TreeNode?, low: Int, high: Int): TreeNode? {
        root ?: return null
        if(root.`val`>high){
            val left=trimBST(root.left,low,high)
            return left
        }
        if(root.`val`<low){
            val right=trimBST(root.right,low,high)
            return right
        }

        root.left=trimBST(root.left,low,high)
        root.right=trimBST(root.right,low,high)
        return root
    }

    fun deleteNode(root: TreeNode?, key: Int): TreeNode? {

        root ?: return root


        if(key==root.`val`){
            if(root.left==null && root.right==null){
                return null
            }else if(root.left!=null && root.right==null){
                return root.left
            }else if(root.left==null && root.right!=null){
                return root.right
            } else{ //
                val originalLeft=root.left
                val originalRight=root.right
                var a=originalRight
                while(a!=null && a.left!=null){
                    a=a.left
                }
                a!!.left=originalLeft
                return originalRight
            }
        }

        if(key<root.`val`){
            root.left=deleteNode(root.left,key)
        }


        if(key>root.`val`){
            root.right=deleteNode(root.right,key)
        }

        return root

    }

    var parent_insertIntoBST: TreeNode? = null

    var res: TreeNode? = null

    var onceLock = true

    fun insertIntoBST(root: TreeNode?, `val`: Int): TreeNode? {

        if (onceLock) {
            onceLock=false
            res = root
        }

        parent_insertIntoBST = root

        if (root == null) {
            if (parent_insertIntoBST == null)
                return TreeNode(`val`)


            val new = TreeNode(`val`)
            if (`val` < parent_insertIntoBST!!.`val`)
                parent_insertIntoBST!!.left = new
            else
                parent_insertIntoBST!!.right = new

            return res

        }

        if (`val` < root!!.`val`) {
            insertIntoBST(root.left, `val`)
        }

        if (`val` > root!!.`val`) {
            insertIntoBST(root.right, `val`)
        }

        return null

    }

    fun insertIntoBSTt(root: TreeNode?, `val`: Int): TreeNode? {
        root?: return TreeNode(`val`)
        var cur=root
        var parent=root
        while(cur!=null){
            parent=cur
            if(cur.`val`<`val`)
                cur=cur.right
            else
                cur=cur.left

        }

        if(parent!!.`val`<`val`)
            parent.right= TreeNode(`val`)
        else
            parent.left= TreeNode(`val`)

        return root

    }

    fun lowestCommonAncestor(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode?{
        root ?: return null
        p ?: return q
        q ?: return p

        var cur=root

        while(cur!=null){
            if(p.`val`< root.`val` && root.`val` > q.`val`){
                cur=root.left
            }else if(q.`val` > root.`val` && root.`val`<p.`val`){
                cur=root.right
            }else {
                return  root
            }
        }
        return null
    }

    fun lowestCommonAncestor1(root: TreeNode?, p: TreeNode?, q: TreeNode?): TreeNode?    {

        val deque:Deque<TreeNode> = LinkedList()

        root?: return null

        if(root===q || root==p)
            return root

        val left=lowestCommonAncestor1(root?.left,p,q)
        val right=lowestCommonAncestor1(root?.right,p,q)

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

