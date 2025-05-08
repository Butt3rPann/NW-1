<script setup>
import { onMounted } from 'vue';
import { useRoute, useRouter} from 'vue-router'
import { ref } from 'vue';
import { editItem, getItemById } from '@/libs/fetchUtils';
import SaleItemForm from './SaleItemForm.vue';

const router = useRouter()

const { params: { id } } = useRoute()

const saleItem = ref({})

onMounted( async () => {
    try {
        saleItem.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, id)
    } catch (error) {
        console.log(error);
    }
})

async function editSaleItem(editedItem) {
    try {
        await editItem(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, id, editedItem)
    } catch (error) {
        console.log(error);
    }
}

const handleEditSaleItem = async (editedItem) => {
    await editSaleItem(editedItem)
    router.push(`/sale-items/${id}`);
}

</script>
 
<template>
    <div>
        <SaleItemForm v-if="saleItem.id" @submitAction="handleEditSaleItem" :saleItemData="saleItem"/>
    </div>
</template>
 
<style scoped>

</style>