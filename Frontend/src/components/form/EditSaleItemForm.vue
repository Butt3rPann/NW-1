<script setup>
import { onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router'
import { ref } from 'vue';
import { editItem, getItemById } from '@/libs/fetchUtils';
import SaleItemForm from './SaleItemForm.vue';

const router = useRouter()

const disabled = ref(false)

const { params: { id } } = useRoute()

const saleItem = ref({})
const originalSaleItem = ref({})

onMounted(async () => {
    try {
        const result = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/sale-items`, id)
        saleItem.value = result
        originalSaleItem.value = JSON.parse(JSON.stringify(result))
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
    const oldData = JSON.stringify(originalSaleItem.value)
    const newData = JSON.stringify(editedItem)

    if (oldData === newData) {
        disabled.value = true
        return
    }

    try {
        await editSaleItem(editedItem)
        disabled.value = true
        router.push({ path: `/sale-items/${id}`, query: { edited: 'true' } })
    } catch (error) {
        console.log(error)
    }
}

</script>

<template>
    <div>
        <SaleItemForm v-if="saleItem.id" @submitAction="handleEditSaleItem" :saleItemData="saleItem"
            :path="`/sale-items/${id}`" :disabled="disabled" />
    </div>
</template>

<style scoped></style>