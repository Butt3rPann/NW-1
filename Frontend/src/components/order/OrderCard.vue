<script setup>
import calendar from "@/assets/images/calendar.png"
import creditcard from "@/assets/images/creditcard.png"

const props = defineProps({
    orders: {
        type: Array,
        required: true
    }
})

const totalPrice = (order) => {
    return order.orderItems.reduce((sum, item) => sum + item.price * item.quantity, 0)
}

</script>
 
<template>
<div class="rounded-lg text-[#332A1E] p-5 border space-y-5 border-gray-300 cursor-pointer transition duration-200">
    <div v-for="(order, index) in orders" :key="index" class="itbms-row rounded-lg p-5 border border-gray-300 cursor-pointer hover:shadow-lg transition duration-200 bg-white">
      <router-link :to="{ name: 'OrderDetail', params: { orderId: order.orderItems[0].no } }">
        <div class="border-b border-[#ABBCC9]">
          <div class="flex justify-between items-center mb-3">
            <div>
              <p class="font-bold text-lg">Order # <span class="itbms-order-id">{{ order.orderItems[0].no }}</span></p>
              <div class="flex items-center mb-3 mt-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20">
                  <path fill="#332A1E" d="M6.123 7.25L6.914 2H2.8L1.081 6.5q-.08.24-.081.5c0 1.104 1.15 2 2.571 2c1.31 0 2.393-.764 2.552-1.75M10 9c1.42 0 2.571-.896 2.571-2q-.001-.062-.005-.121L12.057 2H7.943l-.51 4.875L7.429 7c0 1.104 1.151 2 2.571 2m5 1.046V14H5v-3.948c-.438.158-.92.248-1.429.248c-.195 0-.384-.023-.571-.049V16.6c0 .77.629 1.4 1.398 1.4H15.6c.77 0 1.4-.631 1.4-1.4v-6.348a4 4 0 0 1-.571.049A4.2 4.2 0 0 1 15 10.046M18.92 6.5L17.199 2h-4.113l.79 5.242C14.03 8.232 15.113 9 16.429 9C17.849 9 19 8.104 19 7q-.001-.26-.08-.5" />
                </svg>
                <p class="itbms-nickname ml-1">{{ order.seller.userName }}</p>
              </div>              
            </div>
            <div>
              <div class="flex justify-end items-center">
                <img :src="calendar" alt="calendarIcon" class="w-4">
                <p class="mr-1 ml-2">Order Date:</p>
                <p class="itbms-order-date text-sm text-gray-500">
                  {{ new Date(order.orderDate).toLocaleDateString() }}
                </p>
              </div>
              <div class="flex justify-end items-center">
                <img :src="creditcard" alt="cresitcardIcon" class="w-5">
                <p class="mr-1 ml-2">Payment Date:</p>
                <p class="itbms-payment-date text-sm text-gray-500">
                  {{ new Date(order.paymentDate).toLocaleDateString() }}
                </p>          
              </div>            
            </div>
          </div>
        </div>

        <div class="mt-3 mb-5 border-b border-[#ABBCC9] pb-5">
          <p class="font-semibold mb-2">ITEMS:</p>
          <ul class="space-y-1">
            <li
              v-for="(item, index) in order.orderItems"
              :key="index"
              class="itbms-item-row flex justify-between text-base"
            >
              <div class="flex items-center">
                <div class="bg-[#FAF6F5] rounded-sm w-25 h-20 flex justify-center items-center">
                  <img src="/saleItemImage/demoImg1.png" class="max-w-12 max-h-15"/>
                </div>
                <p class="itbms-item-description ml-3">{{ item.description }}</p>
                <p class="ml-1">(x<span class="itbms-item-quantity">{{ item.quantity }}</span>)</p>
              </div>
              <p>฿ <span class="itbms-item-total-price">{{ (item.price * item.quantity).toLocaleString() }}</span></p>
            </li>
          </ul>
        </div>

        <div class="mb-5 text-sm text-gray-600">
          <p class="font-semibold">Address: <span class="itbms-shipping-address"> {{ order.shippingAddress }}</span></p>
          <p v-if="order.orderNote" class="font-semibold mt-1">Note: <span class="itbms-order-note"> {{ order.orderNote }}</span></p>
        </div>

        <div class="flex justify-between items-center border-t border-[#ABBCC9] pt-3 mt-3">
          <p
            class="itbms-order-status px-3 py-1 text-xs font-semibold rounded-full"
            :class="order.orderStatus === 'COMPLETED' ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700'"
          >
            {{ order.orderStatus }}
          </p>
          <p class="font-bold text-lg text-green-700">
            ฿
            <span class="itbms-total-order-price">
              {{ totalPrice(order).toLocaleString() }}
            </span>
          </p>
        </div>        
      </router-link>
    </div>
</div>
</template>
 
<style scoped>

</style>