<script>
import {checkPayment} from '../api/payment'
import Vue from 'vue'
import {getRentInfoById} from '../api/rent'

export default {
  data () {
    return {
      rentInfo: [],
      confirmRequest: {
        'transactionId': '',
        'paymentId': '',
        'amount': null,
        'currency': 'TWD'
      }
    }
  },
  created () {
    this.confirmPayment()
  },
  methods: {
    async confirmPayment () {
      try {
        const rentInfoId = localStorage.getItem('rentInfoId')
        const rentId = localStorage.getItem('rentId')

        if (!rentInfoId || !rentId) {
          throw new Error('找不到租屋訂單id或訂單明細id')
        }

        const rentInfoResponse = await getRentInfoById(rentInfoId)
        Vue.prototype.$message({message: '付款成功,結束將導向至訂單頁面', type: 'success'})
        console.log('取得租屋資訊成功', rentInfoResponse)

        this.rentInfo = rentInfoResponse
        this.confirmRequest.amount = rentInfoResponse.amount
        this.confirmRequest.transactionId = rentInfoResponse.transactionId
        this.confirmRequest.paymentId = rentInfoResponse.paymentId

        const checkPaymentResponse = await checkPayment(rentId, this.confirmRequest)
        console.log('確認訂單成功', checkPaymentResponse)

        localStorage.removeItem('rentInfoId')
        localStorage.removeItem('rentId')

        this.$router.push('/rent-order')
      } catch (err) {
        Vue.prototype.$message({message: '付款失敗，請重新付款', type: 'error'})
        console.error('確認訂單失敗', err)
        this.$router.push('/rent-order')
      }
    }
  }
}
</script>

<template>
  <h1>付款成功，正在確認中...結束將導向至訂單頁面</h1>
</template>

<style scoped>
</style>
