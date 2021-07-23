package com.aaaaa.a2k

import androidx.annotation.MainThread
import androidx.room.PrimaryKey
import kotlinx.coroutines.processNextEventInCurrentThread
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleTest2 {





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



}