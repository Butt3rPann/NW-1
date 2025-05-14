<script setup>
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router'
import { ref } from 'vue';
import { editItem, getItemById } from '@/libs/fetchUtils';
import SaleItemForm from './SaleItemForm.vue';
import SaleItemNotFound from '@/components/sale-item/SaleItemNotFound.vue';

const router = useRouter()
const { params: { id } } = useRoute()
const saleItem = ref({})

onMounted(async () => {
    try {
        saleItem.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, id)
    } catch (error) {
        console.log(error);
    }
})

const handleEditSaleItem = async (editedItem) => {
    try {
        await editItem(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, id, editedItem)
        router.push({ name: 'SaleItemsDetail', params: { saleItemId: id}, query: { edited: 'true' } })
    } catch (error) {
        console.log(error)
    }
}
</script>

<template>
    <div class="px-25 gap-15 mt-35 mb-20 font-rubik">
        <p class=" font-medium text-lg mb-7">
            <router-link :to="{ name: 'SaleItems' }"><span class="text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <router-link :to="{ name: 'SaleItemsDetail', params: { saleItemId: id } }"><span class="itbms-back-button text-[#6F879C]">{{ saleItem.model }}</span></router-link>
        </p>
        <SaleItemForm v-if="saleItem.id" @submitAction="handleEditSaleItem" :saleItemData="saleItem"
            :path="`/sale-items/${id}`"/>
        <SaleItemNotFound v-else />
    </div>
</template>

<style scoped></style>