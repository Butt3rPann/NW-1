<script setup>
import { useRouter } from 'vue-router'
import SaleItemForm from '@/components/form/SaleItemForm.vue'
import { addItem } from '@/libs/fetchUtils'

const router = useRouter()
const emit = defineEmits(['addSuccess'])

const handleNewSaleItem = async (newSaleItem) => {
    try {
        await addItem(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, newSaleItem)
        router.push({ name: 'SaleItems', query: { added: 'true' } })
    } catch (error) {
        console.log(error)
    }
}
</script>

<template>
    <div class="px-25 mt-35 mb-20 font-rubik">
        <p class=" font-medium text-lg mb-7">
            <router-link :to="{ name: 'SaleItems' }"><span class="text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <span class="text-[#6F879C]">New Sale Item</span>
        </p>
        <SaleItemForm @submitAction="handleNewSaleItem" path="/sale-items"/>
    </div>
</template>

<style scoped></style>
