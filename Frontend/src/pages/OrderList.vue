<script setup>
import OrderCard from '@/components/order/OrderCard.vue'
import { getItemByIdWithToken } from '@/libs/fetchUtils'
import { useUserStore } from '@/stores/UserStore'
import { ref, onMounted, computed } from 'vue'
import emptyOrdersImg from '@/assets/images/emptySaleItems.png'
import Pagination from '@/components/elements/Pagination.vue'

const userStore = useUserStore()
const { getUserId, getAccessToken } = userStore
const id = ref(0)
const orders = ref([])
const response = ref({})
const currentPage = ref(1)
const currentSize = ref(3)
const totalPage = ref(0)
const ordersComplete = computed(() => 
  orders.value.filter(order => order.orderStatus === 'COMPLETED')
)
const currentTab = ref('complete')
const tabs = ['complete','canceled']

const goToPage = async (page) => {
    currentPage.value = page
    await getSellerOrders()
}

async function getAllOrders() {
    id.value = getUserId()
    if(id.value) {
        try {
            response.value = await getItemByIdWithToken(
                `${import.meta.env.VITE_APP_URL}/v2/users/${id.value}/orders`, 
                getAccessToken(), 
                currentPage.value - 1,
                currentSize.value
            )
            orders.value = response.value.content
            totalPage.value = response.value.totalPages
            
        } catch (error) {
            console.log(error)
        }
    }
}

onMounted(async () => {
    try {
        await getAllOrders()
    } catch (error) {
        console.log(error)
    }
})
</script>
 
<template>
<div class="w-full min-h-screen font-rubik bg-white p-12 pl-20 pr-20 text-[#332A1E]">
    <p class="font-extrabold text-4xl mt-20 mb-5">All orders</p>
    <div>
        <button v-for="tab in tabs" :key="tab"
        :class="[
        'pb-2 mr-6 mb-5 font-semibold transition duration-200',
        currentTab === tab ? 'border-b-4 border-[#332A1E]' : 'text-gray-400 hover:text-[#332A1E]',
        tab === 'complete' ? 'itbms-completed-orders-button' :  'itbms-canceled-orders-button'
        ]">
            {{ tab === 'complete' ? 'All Orders': 'Canceled Orders' }}
        </button>
    </div>
    <OrderCard v-if="ordersComplete.length" :orders="ordersComplete"/>
    <div v-else class="flex flex-col items-center space-y-3 py-18">
        <img :src="emptyOrdersImg" alt="EmptySaleItems" class=" w-36">
        <p class="text-xl text-[#ABBCC9]">no order</p>
    </div>
    <Pagination :totalPage="totalPage" :currentPage="currentPage" :currentSize="currentSize" @changePage="goToPage"/>
</div>
</template>
 
<style scoped>

</style>