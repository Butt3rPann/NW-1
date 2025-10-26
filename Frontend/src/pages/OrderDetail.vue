<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getItemById } from '@/libs/fetchUtils.js'
import { useUserStore } from '@/stores/UserStore'

const userStore = useUserStore()
const { getAccessToken } = userStore

const { params: { orderId } } = useRoute()

const selectedOrder = ref({})
const previousPath = ref('')

const route = useRoute()
onMounted(() => {
    if (window.history.state?.back) {
        previousPath.value = window.history.state.back
    }
})

onMounted(async () => {
    try {
       selectedOrder.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/orders`, orderId, getAccessToken())
       selectedOrder.value.orderItems.forEach(item => {
            if (item.image) {
                item.image = `${import.meta.env.VITE_APP_URL}/v1/files/${item.image}?t=${Date.now()}`
            }
        })
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
<div class="w-full min-h-screen font-rubik bg-white pl-7 pr-7 pb-10 sm:p-10 sm:pl-12 sm:pr-12 lg:p-12 lg:pl-20 lg:pr-20 text-[#332A1E]">
    <div class="itbms-row">
        <div class="mt-20 mb-5">
            <p class="font-medium text-sm md:text-base lg:text-lg mb-7">
                <router-link v-if="previousPath.includes('sale-orders')" :to="{ name: 'SaleOrderList', query: { tab: route.query.tab || 'new' } }"><span class="itbms-sale-orders-button text-[#332A1E] cursor-pointer">Seller Orders List</span></router-link>
                <router-link v-else :to="{ name: 'OrderList', query: { tab: route.query.tab || 'completed' } }"><span class="itbms-your-orders-button text-[#332A1E] cursor-pointer">Your Orders</span></router-link>
                <span class="text-[#332A1E]/50 mx-3"> > </span>
                <router-link :to="{ name: 'OrderDetail'}"><span class="itbms-back-button text-[#6F879C]">Order Details</span></router-link>
            </p>
            <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
                <div class="flex flex-wrap items-center gap-2">
                    <p class="text-2xl sm:text-3xl lg:text-4xl font-bold">Order # <span class="itbms-order-id">{{ orderId }}</span></p>
                    <p
                    class="itbms-order-status px-3 py-1 ml-2 text-xs font-semibold rounded-full"
                    :class="selectedOrder.orderStatus === 'COMPLETED' ? 'bg-green-100 text-green-700' : 'bg-yellow-100 text-yellow-700'"
                    >
                    {{ selectedOrder.orderStatus }}
                    </p>                     
                </div>
            </div>
        </div>
        <div class="grid grid-cols-1 lg:grid-cols-3 gap-4 lg:gap-6">
            <div class="lg:col-span-2 space-y-4 overflow-hidden">
                <div class="p-4 sm:p-5 bg-white border border-[#ABBCC9] rounded-lg shadow-2xs">
                    <p class="text-lg sm:text-xl font-bold mb-3 sm:mb-4">Order Items</p>
                    <ul class="space-y-1 border-b border-gray-300 pb-4">
                    <li
                        v-for="(item, index) in selectedOrder.orderItems"
                        :key="index"
                        class="itbms-item-row flex justify-between items-start sm:items-center gap-3"
                    >
                        <div class="flex items-start sm:items-center gap-3 flex-1 min-w-0">
                            <div class="bg-[#FAF6F5] rounded-sm w-16 h-16 sm:w-20 sm:h-20 flex-shrink-0 flex justify-center items-center">
                                <p v-if="!item.image" class="text-[#332A1E] text-[0.74em] md:text-sm xl:text-base">No Picture</p>
                                <img v-else :src="item.image" alt="SaleItem Image" class="max-w-10 max-h-13 sm:max-w-12 sm:max-h-15"/>
                            </div>
                            <div class="flex flex-col ml-3 min-w-0 flex-1">
                                <p class="itbms-item-description text-sm sm:text-base">{{ item.description }} <span class="ml-1">(x<span class="itbms-item-quantity">{{ item.quantity }}</span>)</span></p>
                                <p class="text-gray-500 text-xs sm:text-sm mt-1">Unit Price: <span class="itbms-item-price">{{ item.price.toLocaleString() }}</span></p>
                            </div>
                        </div>
                        <p class="font-medium text-base sm:text-lg whitespace-nowrap">฿ <span class="itbms-item-total-price">{{ (item.price * item.quantity).toLocaleString() }}</span></p>
                    </li>
                    </ul>
                    <div class="flex justify-between items-center mt-4">
                    <p class="font-bold text-base sm:text-lg">Total</p>
                    <p class="font-bold text-xl sm:text-2xl">฿ <span class="itbms-total-order-price">{{ totalPrice(selectedOrder).toLocaleString() }}</span></p>  
                    </div>              
                </div>
                <div class="p-4 sm:p-5 bg-white border border-[#ABBCC9] rounded-lg shadow-2xs">
                    <p class="text-lg sm:text-xl font-bold mb-3">Delivery Address</p>
                    <p class="itbms-shipping-address text-sm sm:text-base">{{ selectedOrder.shippingAddress }}</p>
                    <p v-show="selectedOrder.orderNote" class="text-sm sm:text-base font-bold mb-3 mt-3 border-t pt-3 border-gray-300">Note: <span class="itbms-order-note font-medium">{{ selectedOrder.orderNote }}</span></p>
                </div>
            </div>
            <div class="space-y-4 overflow-hidden">
                <div class="p-4 sm:p-5 bg-white border border-[#ABBCC9] rounded-lg shadow-2xs">
                    <p class="text-lg sm:text-xl font-bold mb-3 sm:mb-4">Seller</p>
                    <div class="border-b pb-3 border-gray-300">
                        <p class="text-xs sm:text-sm text-gray-500">Name</p>
                        <p class="itbms-nickname font-medium mt-1 text-base sm:text-lg">{{ selectedOrder.seller?.nickName }}</p>
                    </div>
                    <div class="pb-3 mt-3">
                        <p class="text-xs sm:text-sm text-gray-500">Email</p>
                        <p class="font-medium mt-1 text-sm sm:text-base">{{ selectedOrder.seller?.email }}</p>
                    </div>
                </div>
                <div class="p-4 sm:p-5 bg-white border border-[#ABBCC9] rounded-lg shadow-2xs">
                    <p class="text-lg sm:text-xl font-bold mb-3 sm:mb-4">Order Info</p>
                    <div class="border-b pb-3 border-gray-300">
                        <p class="text-xs sm:text-sm text-gray-500">Order Date</p>
                        <p class="itbms-order-date font-medium mt-1 text-base sm:text-lg">{{ new Date(selectedOrder.orderDate).toLocaleDateString() }}</p>
                    </div>
                    <div class="pb-2 mt-3">
                        <p class="text-xs sm:text-sm text-gray-500">Payment Date</p>
                        <p class="itbms-payment-date font-medium mt-1 text-base sm:text-lg">{{ new Date(selectedOrder.paymentDate).toLocaleDateString() }}</p>
                    </div>
                </div>
            </div>
        </div>        
    </div>
</div>
</template>
 
<style scoped>

</style>