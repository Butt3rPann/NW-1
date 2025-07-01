<script setup>
import { useRouter } from 'vue-router'
import SaleItemForm from '@/components/form/SaleItemForm.vue'
import { addItem } from '@/libs/fetchUtils'
import { ref } from 'vue'

const router = useRouter()

const prevPath = ref(null)
const prevPathName = ref(null)
prevPath.value = router.options.history.state.back
if (prevPath.value) {
    const resolve = router.resolve(prevPath.value)
    prevPathName.value = resolve.name
} else {
    prevPathName.value = 'SaleItems'
}

const handleNewSaleItem = async (newSaleItem) => {
    try {
        await addItem(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, newSaleItem)
        router.push({ name: prevPathName.value, query: { added: 'true' } })
    } catch (error) {
        console.log(error)
    }
}
</script>

<template>
    <div class="px-10 md:px-22 lg:px-25 pt-35 pb-20 font-rubik bg-white">
        <p class="font-medium text-sm md:text-base lg:text-lg mb-7">
            <router-link :to="{ name: 'SaleItems' }"><span class="text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-2 md:mx-3"> > </span>
            <span class="text-[#6F879C]">New Sale Item</span>
        </p>
        <SaleItemForm @submitAction="handleNewSaleItem" :pathName="prevPathName"/>
    </div>
</template>

<style scoped></style>
