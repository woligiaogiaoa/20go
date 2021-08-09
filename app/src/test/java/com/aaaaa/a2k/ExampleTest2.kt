package com.aaaaa.a2k

import org.junit.Test
import java.lang.reflect.InvocationTargetException
import kotlin.math.abs
import kotlin.math.min

class TreeNode(var `val`: Int) {
         var left: TreeNode? = null
         var right: TreeNode? = null
     }



class ExampleTest2 {

    /*输入：
[7,1,5,3,6,4]
输出：
7
预期结果：
5*/

    fun maxProfittt(prices: IntArray): Int {


        if(prices.size<=1)
            return 0

        fun max(a: Int, b: Int)=Math.max(a, b)

        val dp= mutableListOf<IntArray>().apply {
                    repeat(prices.size){
                        add(IntArray(2))
                    }
        }
        //0 1 2 3 不做任何操作 买 卖 //冷冻期
        dp[0][1]=-prices[0]
        dp[0][0]=0
        for(i in 1..prices.size-1){
            dp[i][0]=max(dp[i-1][0],dp[i-1][1]+prices[i])
            dp[i][1]=max(dp[i-1][1],dp[i-1][0]-prices[i])
        }
        return dp[prices.size-1][0]
    }

    fun maxProfit(prices: IntArray): Int {
        fun max(a: Int, b: Int)=Math.max(a, b)

        if(prices.size<=1)
            return 0

        val dp= mutableListOf<IntArray>().apply {
            repeat(prices.size){
                add(IntArray(4))
            }
        }
        // 0 1 2 3 //没买
        dp[0][0]=0
        dp[0][1]=-prices[0]
        dp[0][2]=0
        dp[0][3]=0
        for(i in 1..prices.size-1){
            dp[i][0]=max(dp[i-1][0],dp[i-1][3])
            dp[i][1]=max(-prices[i]+max(dp[i-1][0],dp[i-1][3]),dp[i-1][1] )
            dp[i][2]=prices[i]+dp[i-1][1]
            dp[i][3]=dp[i-1][2]
        }

        return max(dp[prices.size-1][2],max(dp[prices.size-1][0],dp[prices.size-1][3]))
    }

    fun maxProfi11t(prices: IntArray): Int {
        fun max(a: Int, b: Int)=Math.max(a, b)
        if(prices.size<=1)
            return 0
        val dp= mutableListOf<IntArray>().apply {
            repeat(prices.size){
                add(IntArray(2))
            }
        }
        dp[0][1]=0
        dp[0][0]= -prices[0]
        for(i in 0..prices.size-1){
            dp[i][0]=max(dp[i-1][0],-prices[i])
            dp[i][1]=max(dp[i-1][0]+prices[i],dp[i-1][1] )//dp[i-1][1]
        }
        return dp[prices.size-1][1]
    }

    fun maxProfit11(prices: IntArray): Int {
            fun max(a: Int, b: Int)=Math.max(a, b)
            if(prices.size<=1)
                return 0
            val dp= mutableListOf<IntArray>().apply {
                repeat(prices.size){
                    add(IntArray(2))
                }
            }
            dp[0][1]=-prices[0]
            dp[0][0]= 0
            for (index in 1..prices.size-1){
                if(index!=0){
                    dp[index][1]=max(dp[index-1][1],dp[index-1][0]-prices[index])
                    dp[index][0]= max(dp[index-1][0],dp[index-1][1]+prices[index])
                }
            }
            return dp[prices.size-1][0]
        }

    fun maxProfitIIIwrong(prices: IntArray): Int {
        fun max(a: Int, b: Int)=Math.max(a, b)
        fun min(a: Int, b: Int)=Math.min(a, b)
        if(prices.size<=1)
            return 0
        if(prices.size<=3){
            val res=prices.run {
                var first=true
                var last=0
                var acc=0
                forEach {
                    if(first){
                        first=false
                        last=it
                        return@forEach
                    }
                    if(it-last>0)
                        acc+= it-last
                    last=it
                }
                acc
            }
            return res
        }
        var lowest=Int.MAX_VALUE

        val resArray=IntArray(2)
        for (i in 0..prices.size-1) {
            lowest= min(lowest,prices[i])
            val can=prices[i]-lowest
            if(can<=0) continue

            if(can>=resArray[1]){ //yao gengxin  can>0
                if(resArray[0]==0){
                    //first
                    resArray[0]=can
                    continue
                }else{
                    if(resArray[1]==0){
                        val big=if(resArray[0]>= can) resArray[0] else can
                        val small=if(resArray[0]>= can) can else resArray[0]
                        resArray[0]=big
                        resArray[1]=small
                        continue
                    }else{
                        if(can<=resArray[1])
                            continue
                        if(can<=resArray[0]){
                            resArray[1]=can
                            continue
                        }
                        resArray[1]=resArray[0]
                        resArray[0]=can
                    }
                }
            }
        }
        return resArray[0]+resArray[1]
    }

    fun maxProfitIII(prices: IntArray): Int {
        fun max(a: Int, b: Int)=Math.max(a, b)
        fun min(a: Int, b: Int)=Math.min(a, b)
        if(prices.size<=1)
            return 0
        // dp[][0] dp[][1] dp[][2] dp[3] dp[][4]
        val dp= mutableListOf<IntArray>().apply {
            repeat(prices.size){
                add(IntArray(5))
            }
        }
        dp[0][0]=0
        dp[0][1]=-prices[0]
        dp[0][2]=0
        dp[0][3]=-prices[0]
        dp[0][4]=0
        for(i in 1..prices.size-1){
            dp[i][0]=0
            dp[i][1]=max(dp[i-1][0]- prices[i],dp[i-1][1])
            dp[i][2]=max(dp[i-1][1]+prices[i] ,dp[i-1][2])
            dp[i][3]=max(dp[i-1][3],dp[i-1][2]-prices[i])
            dp[i][4]=max(dp[i-1][4],dp[i-1][3]+prices[i])
        }


        return dp[prices.size-1][4]
    }

    fun maxProfitKTimes(k: Int, prices: IntArray): Int {
        fun max(a: Int, b: Int)=Math.max(a, b)
        fun min(a: Int, b: Int)=Math.min(a, b)
        if(prices.size<=1)
            return 0
        if(k<=0) return 0
        val dp= mutableListOf<IntArray>().apply {
            repeat(prices.size){
                add(IntArray(1 + 2 * k))
            }
        }
        dp[0][0]=0
        for(i in 1..2*k){
            if(i %2 == 0){
                dp[0][i]=0
            }else{
                dp[0][i]=-prices[0]
            }
        }
        for(i in 1..prices.size-1){
            dp[i][0]=0
            for( j in 1..2*k){
                val state= j
                val sold =  state % 2 ==0 //卖出去的
                if(sold){
                    dp[i][state]= max(dp[i-1][state],dp[i-1][state-1] + prices[i])
                }else{
                    dp[i][state] =max(dp[i-1][state] ,dp[i-1][state-1]-prices[i])
                }
            }
        }
        return dp[prices.size-1][2*k]
    }


    fun findMaxForm(strs: Array<String>, m: Int, n: Int): Int  {
     //dp[m][n]  before or new limit
     // dp[m ][n ]  under this limit max component

     fun max(a: Int, b: Int)=Math.max(a, b)

     val dp = mutableListOf<IntArray>().apply {
         repeat(m + 1) {
             add(IntArray(n + 1))
         }
     }
     //dp[m][n]
     dp[0][0] = 0


     fun String.compute01(): Pair<Int, Int> = kotlin.run {
         var m=0
         var n=0
         forEach { it ->
             if(it=='0')
                 m++
             if(it=='1')
                 n++
         }
         Pair(m, n)
     }

     for (i in 0..strs.size - 1) {
         val item01: Pair<Int, Int> =strs[i].compute01()
         //dp[m][n] or dp[m-item01.first][n-item01.second]+1
         for(x in m downTo item01.first){
             //
             for(y in n downTo item01.second){
                 dp[x][y]= max(dp[x][y], dp[x - item01.first][y - item01.second] + 1)
             }
         }
     }
     return dp[m][n]
 }
    /*dp[4]= dp[2]+1=2 dp[6] =1+dp[4]
    * */

    fun rob1(root: TreeNode?): Int {
        fun max(a: Int, b: Int)=Math.max(a, b)
        root?: return 0
        var val1 =root.`val`
        if(root.left!=null){
            val1+=rob1(root!!.left!!.left) + rob1(root!!.left!!.right)

        }
        if(root.right!=null){
            val1+=rob1(root!!.right?.left) +rob1(root!!.right?.right)
        }

        val val2=rob1(root.left) + rob1(root.right)

        return max(val1, val2)
    }

    fun rob2(root: TreeNode?): Int {

        fun max(a: Int, b: Int)=Math.max(a, b)
        root?: return 0
        var res= 0

        val dp1 =IntArray(2)

      /*  fun t(root: TreeNode?){
            root?: return
            t(root.left)
            t(root.right)
            root
        }*/

        fun dp(n: TreeNode?, contains: Boolean):Int{ //return robbed value
            n ?: return 0
            if(contains){
                return dp(n.left, false)+dp(n.right, false) + n.`val`
            }else{
                return max(dp(n.left, true), dp(n.left, false)) +
                        max(dp(n.right, true), dp(n.right, false))
            }
        }
        return max(dp(root, true), dp(root, false))
    }

    fun rob3(root: TreeNode?): Int {
        val res = robAction1(root)
        return Math.max(res[0], res[1])
    }

    fun robAction1(root: TreeNode?): IntArray {
        val res = IntArray(2)
        if (root == null) return res
        val left = robAction1(root.left)
        val right = robAction1(root.right)
        res[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1])
        res[1] = root.`val` + left[0] + right[0]
        return res
    }

    fun rob(root: TreeNode?): Int {

        fun max(a: Int, b: Int)=Math.max(a, b)

        fun robDp(root: TreeNode?):IntArray{
            root ?: return IntArray(2)
            val left= robDp(root.left)
            val right= robDp(root.right)
            val res= IntArray(2)
            res[0]= max(left[0],left[1])+ max(right[0],right[1])
            res[1]= root.`val`+left[0] + right[0]
            return  res
        }

        val a= robDp(root)
        return max(a[0],a[1])

    }



    fun rob(nums: IntArray): Int {

        if(nums.isEmpty()) return 0

        if(nums.size==1) return nums[0]

        val dp1= mutableListOf<IntArray>().apply {
            repeat(nums.size){
                add(IntArray(2))
            }
        }

     /*   dp1[0][1]=0//nums[0]
        dp1[0][0]=0*/
        dp1[1][0]=0
        dp1[1][1]=nums[1]
        /*   dp [index][0]= dp[index-1][0]+nums[index] or dp[index-1][1]
                   dp[index][1]= dp[index-1][0] + nums[index]*/
        for(index in 2..nums.size-1){

            dp1[index][0]= Math.max(dp1[index - 1][0], dp1[index - 1][1])
            dp1[index][1]= dp1[index - 1][0] + nums[index]
        }

        val last=nums.size-1
        val res1 = Math.max(dp1[last][0], dp1[last][1])

        if(nums.size>2){
            dp1[2][0]=0
            dp1[2][1]=nums[2]
        }else{
            return Math.max(nums[0], res1)
        }


        for(index in 3..nums.size-1){

            dp1[index][0]= Math.max(dp1[index - 1][0], dp1[index - 1][1])
            dp1[index][1]= dp1[index - 1][0] + nums[index]
        }

        val res2  = dp1[last][0]

        return Math.max(res1, res2 + nums[0])

    }


    fun rob1(nums: IntArray): Int {

        if(nums.isEmpty()) return 0

        val dp= mutableListOf<IntArray>().apply {
            repeat(nums.size){
                add(IntArray(2))
            }
        }

        dp[0][1]=nums[0]
        dp[0][0]=0
     /*   dp [index][0]= dp[index-1][0]+nums[index] or dp[index-1][1]
                dp[index][1]= dp[index-1][0] + nums[index]*/
        for(index in 1..nums.size-1){

            dp[index][0]= Math.max(dp[index - 1][0], dp[index - 1][1])
            dp[index][1]= dp[index - 1][0] + nums[index]
        }

        val last=nums.size-1
        return Math.max(dp[last][0], dp[last][1])
    }

    fun String.ishuiwen(): Boolean {
        var i=0;
        var j =length-1
        while(i<j){
            if(!(this[i]==this[j]) )
                return false
            i++
            j--
        }
        return true
    }

    fun wordBreak1(s: String, wordDict: List<String>): Boolean {

        var found =false
        wordDict.forEach { item ->
            if(found)
                return true
            if(s.indexOf(item)==0){
                if(  item.length <s.length ){
                   if(wordBreak(s.substring(item.length, s.length), wordDict)){
                       found=true
                   }
                }else{
                    return true
                }
            }
        }

        return found
    }

    fun wordBreak(s: String, wordDict: List<String>): Boolean{
        //dp[j] : length=nengbunneg huznagman
        val dp= IntArray(s.length + 1){0}
        dp[0]=0 //dpj:容量为j装到 缩少
        for(j in 0..s.length){
            wordDict.forEach { item ->
                if(j>=item.length)
                if(dp[j - item.length]==j-item.length &&
                        item.equals(s.substring(j - item.length, j)))
                    dp[j]=Math.max(dp[j], item.length + dp[j - item.length])
            }
        }
            /* wordDict.forEach { item ->
            for(j in item.length..s.length){
                if(dp[j-item.length]==j-item.length &&
                        item.equals(s.substring(j-item.length,j)))
                dp[j]=Math.max(dp[j],item.length+dp[j-item.length])
            }
        }*/
        return  dp[s.length] == s.length
    }

    fun coinChange(coins: IntArray, amount: Int): Int {
        //dp[i][j] : amount=j min num
        val dp = IntArray(amount + 1) { Int.MAX_VALUE }
        dp[0] = 0
        for (i in 0..coins.size - 1) { //coins[i]
            for (j in coins[i]..amount) {
                if (dp[j - coins[i]] != Int.MAX_VALUE) {
                    dp[j] = Math.min(dp[j], 1 + dp[j - coins[i]])
                }
            }
            println(dp.toMutableList())
            println("size:" + dp.size)
        }
        if (dp[amount] == Int.MAX_VALUE) return -1;
        return dp[amount];
    }

    @Test
    fun teup(){
        coinChange(arrayOf(2, 5, 10, 7).toIntArray(), 27)
    }

    fun numSquares(n: Int): Int {

        val dp = IntArray(n + 1) { Int.MAX_VALUE }
        dp[0] = 0
        //dp[j] 组成的数量最小
        for (i in 1..n) {
            val item = i * i
            if (item > n)
                break
            for (j in 1..n) {
                if (j >= item)
                    if (dp[j - item] < Int.MAX_VALUE)
                        dp[j] = Math.min(dp[j], 1 + dp[j - item])
            }
        }
        if (dp[n] == Int.MAX_VALUE)
            return -1
        return dp[n]
    }

    fun combinationSum4(nums: IntArray, target: Int): Int {

        fun max(a: Int, b: Int)=Math.max(a, b)

        val dp=IntArray(target + 1)
        dp[0]=1
        for(i in 1..target){
            // tong liang i
            /*for(j in nums[i]..target){
                dp[j]=dp[j]+dp[j-nums[i]]
            }*/
            for(j in 0..nums.size-1){
                if(i >= nums[j] ){
                    dp[i]=dp[i]+dp[i - nums[j]]
                }
            }
        }

        return dp[target]
    }

    fun change(amount: Int, coins: IntArray): Int {
        //dp[last][amount]
        //dp[i][j] 0..i suibianqu zucheng j 方法数目
        //dp[i][j]=  dp[i-1][j] + dp[i-1][j-coins[i]]
        fun max(a: Int, b: Int)=Math.max(a, b)

        val dp=IntArray(amount + 1)
        dp[0]=1
        for(i in 0..coins.size-1){
            for(j in coins[i]..amount){
                dp[j]=dp[j]+dp[j - coins[i]]
            }
        }
        return dp[amount]
    }

    fun findTargetSumWays(nums: IntArray, target: Int): Int {

        fun max(a: Int, b: Int)=Math.max(a, b)

        val sum= nums.run {
            var res =0
            forEach {res +=it}
            res
        }
        if(sum-target<0)
            return 0
        if((sum-target)%2!=0)
            return 0

        val y=(sum-target)/2
        // find [sum] =y in nums
        //dp[i][j]

        var res =0
        val dp=IntArray(y + 1){0}

        //mei uping
        dp[0]=1
        // dp[i][j ]  装满 j的最大方法
        for(i in 0..nums.size-1){
            for(j in y downTo nums[i]){
                dp[j]=dp[j]+dp[j - nums[i]]
            }
        }

        return dp[y]

    }

    fun findTargetSumWays1(nums: IntArray, target: Int): Int{
        val path = mutableListOf<Int>()

        var nums1 =0

        fun t(index: Int){ //before all covered
            if(index==nums.size-1 ){
                val sum: Int = path.run {
                    var res =0
                    forEach { res+= it }
                    res
                }

                if(target==sum){
                    nums1++
                    return
                }
            }
            val next =nums[index + 1]
            path.add(next)
            t(index + 1)
            path.removeAt(path.size - 1)

            path.add(-1 * next)
            t(index + 1)
            path.removeAt(path.size - 1)
        }
        t(-1)
        return  nums1
    }

    fun canPartition(nums: IntArray): Boolean {
        if(nums.size<=1){
            return false
        }
        nums.sort()

        val sum1=nums.run {
           var res=0
           forEach {
               res +=it
           }
            res
        }

        if(sum1%2!=0){
            return false
        }

        //capacity
        val capacity =sum1 /2

        fun max(a: Int, b: Int)=Math.max(a, b)

        //dp[0]=1
        val dp=IntArray(capacity + 1)

        //dp[j]
        for(i in 0..nums.size-1){
            nums[i] //add or not add
            for(j in capacity downTo nums[i]){
                dp[j]=max(dp[j], nums[i] + dp[j - nums[i]])
            }
        }
        return  dp[capacity]==capacity


    }

    fun lastStoneWeightII(stones: IntArray): Int {

        fun max(a: Int, b: Int)=Math.max(a, b)

        val sum1=stones.run {
            var res=0
            forEach {
                res +=it
            }
            res
        }


        var capacity=0

        if(sum1%2!=0){
            capacity=sum1/2+1
        }else{
            capacity=sum1/2
        }

        val dp=IntArray(capacity + 1){0}
        //dp[j]
        for(i in 0..stones.size-1){
            stones[i] //add or not add
            for(j in capacity downTo stones[i]){
                dp[j]=max(dp[j], stones[i] + dp[j - stones[i]])
            }
        }

        val part1=dp[capacity]
        return  abs(part1 - sum1 + part1)

    }


    fun bag(){
        //dp[0][1]
        //dp[i][j]
    }

    fun maxProfit(prices: IntArray, fee: Int): Int{

        fun max(a: Int, b: Int)=Math.max(a, b)

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
            dp[i][0]=max(dp[i - 1][0], dp[i - 1][1] + prices[i] - fee)
            dp[i][1]=max(dp[i - 1][1], dp[i - 1][0] - prices[i])
        }

        return dp[prices.size - 1][0]

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
};*/fun maxProfit1(prices: IntArray, fee: Int): Int  {
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