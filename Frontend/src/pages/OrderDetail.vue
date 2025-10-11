<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { getItemById } from '@/libs/fetchUtils.js'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()
const { getAccessToken } = userStore

const { params: { orderId } } = useRoute()

const selectedOrder = ref({})
const previousPath = ref('')

onMounted(() => {
  if (window.history.state?.back) {
    previousPath.value = window.history.state.back
  }
})

onMounted(async () => {
    try {
       selectedOrder.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/orders`, orderId, getAccessToken()) 
    } catch(error) {
        console.log(error)
    }
})

const totalPrice = (order) => {
    if (!order || !order.orderItems) return 0
    return order.orderItems.reduce((sum, item) => sum + item.price * item.quantity, 0)
}
</script>
 
<template>
<div class="w-full min-h-screen font-rubik bg-white p-12 pl-20 pr-20 text-[#332A1E]">
    <div class="itbms-row">
        <div class="mt-20 mb-5">
            <p class="font-medium text-sm md:text-base lg:text-lg mb-7">
                <router-link v-if="previousPath.includes('sale-orders')" :to="{ name: 'SaleOrderList' }"><span class="itbms-your-orders-button text-[#332A1E] cursor-pointer">Seller Orders List</span></router-link>
                <router-link v-else :to="{ name: 'OrderList' }"><span class="itbms-your-orders-button text-[#332A1E] cursor-pointer">Your Orders</span></router-link>
                <span class="text-[#332A1E]/50 mx-3"> > </span>
                <router-link :to="{ name: 'OrderDetail', params: { orderId } }"><span class="itbms-back-button text-[#6F879C]">Order Details</span></router-link>
            </p>
            <div class="flex items-center">
                <p class="text-4xl font-bold">Order # <span class="itbms-order-id">{{ orderId }}</span></p>
                <p
                class="itbms-order-status px-3 py-1 ml-2 text-xs font-semibold rounded-full"
                :class="selectedOrder.orderStatus === 'COMPLETED' ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700'"
                >
                {{ selectedOrder.orderStatus }}
                </p>          
            </div>
        </div>
        <div class="grid grid-cols-3 gap-4">
            <div class="col-span-2 space-y-4 overflow-hidden">
                <div class="p-4 bg-white border-1 border-[#ABBCC9] rounded-lg shadow-2xs">
                    <p class="text-xl font-bold mb-3">Order Items</p>
                    <ul class="space-y-1 border-b border-gray-300 pb-4">
                    <li
                        v-for="(item, index) in selectedOrder.orderItems"
                        :key="index"
                        class="itbms-item-row mt-2 flex justify-between items-center text-base"
                    >
                        <div class="flex items-center">
                        <div class="bg-[#FAF6F5] rounded-sm w-25 h-20 flex justify-center items-center">
                            <img src="/saleItemImage/demoImg1.png" class="max-w-12 max-h-15"/>
                        </div>
                        <div class="flex flex-col ml-3">
                            <p class="itbms-item-description">{{ item.description }} <span class="ml-1">(x<span class="itbms-item-quantity">{{ item.quantity }}</span>)</span></p>
                            <p class="text-sm text-gray-500">Unit Price: <span class="itbms-item-price">{{ item.price.toLocaleString() }}</span></p>
                        </div>
                        </div>
                        <p class="font-medium text-lg">฿ <span class="itbms-item-total-price font-medium text-lg">{{ (item.price * item.quantity).toLocaleString() }}</span></p>
                    </li>
                    </ul>
                    <div class="flex justify-between items-center mt-4">
                    <p class="font-bold text-lg">Total</p>
                    <p class="font-bold text-2xl">฿ <span class="itbms-total-order-price">{{ totalPrice(selectedOrder).toLocaleString() }}</span></p>  
                    </div>              
                </div>
                <div class="p-4 pb-2 bg-white border-1 border-[#ABBCC9] rounded-lg shadow-2xs">
                    <p class="text-xl font-bold mb-3">Delivery Address</p>
                    <p class="itbms-shipping-address">{{ selectedOrder.shippingAddress }}</p>
                    <p v-show="selectedOrder.orderNote" class="text-base font-bold mb-3 mt-3 border-t pt-3 border-gray-300">Note: <span class="itbms-order-note font-medium">{{ selectedOrder.orderNote }}</span></p>
                </div>
            </div>
            <div class="space-y-4 overflow-hidden">
                <div class="p-4 bg-white border-1 border-[#ABBCC9] rounded-lg shadow-2xs">
                    <p class="text-xl font-bold mb-3">Seller</p>
                    <div class="border-b pb-3 border-gray-300">
                        <p class="text-sm text-gray-500">Name</p>
                        <p class="itbms-nickname font-medium mt-1 text-lg">{{ selectedOrder.seller?.nickName }}</p>
                    </div>
                    <div class="pb-3 mt-3">
                        <p class="text-sm text-gray-500">Email</p>
                        <p class="font-medium mt-1 text-base">{{ selectedOrder.seller?.email }}</p>
                    </div>
                </div>
                <div class="p-4 bg-white border-1 border-[#ABBCC9] rounded-lg shadow-2xs">
                    <p class="text-xl font-bold mb-3">Order Info</p>
                    <div class="border-b pb-3 border-gray-300">
                        <p class="text-sm text-gray-500">Order Date</p>
                        <p class="itbms-order-date font-medium mt-1 text-lg">{{ new Date(selectedOrder.orderDate).toLocaleDateString() }}</p>
                    </div>
                    <div class="pb-2 mt-3">
                        <p class="text-sm text-gray-500">Payment Date</p>
                        <p class="itbms-payment-date font-medium mt-1 text-base">{{ new Date(selectedOrder.paymentDate).toLocaleDateString() }}</p>
                    </div>
                </div>
            </div>
        </div>        
    </div>
</div>
</template>
 
<style scoped>

</style>