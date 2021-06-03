package com.aaaaa.a2k

import org.junit.Test

import org.junit.Assert.*

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

        assertEquals("[2,1,4,3]",datas.toString().replace(" ","",true))

    }
}

fun removeNthFromEnd(head: ListNode?, n: Int): ListNode? {

    var slow: ListNode? =head
    var fast: ListNode? =head

    var pre: ListNode =ListNode(-1).apply {
        next=head
    }

    repeat(n-1){
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