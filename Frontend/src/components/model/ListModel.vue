<script setup>
defineProps({
    items: {
        type: Array,
        required: true
    },
    view: {
        type: String,
        default: 'gallery',
        validator: (value) => ['gallery', 'list'].includes(value)
    }
})
</script>

<template>
<div>
    <div :class="view === 'gallery' ? 'grid grid-cols-5 gap-5 mx-auto' : 'flex flex-col gap-7'">
        <div v-for="item in items" :key="item.id" class="itbms-row shadow-[0_0.065rem_0.18rem_0_rgba(0,0,0,0.15)] rounded-md overflow-hidden hover:shadow-[0_0.08rem_0.4rem_rgba(0,0,0,0.15)] bg-white"
            :class="view === 'gallery' ? 'hover:scale-[1.01]' : 'flex flex-row'" >
            <router-link :to="{ name: 'SaleItemsDetail', params: { saleItemId: item.id } }" class="w-full" :class="{'flex gap-10' : view === 'list'}">
                <div class="flex items-center justify-center bg-[#FAF6F5]" :class="{'w-50' : view === 'list'}">
                    <img :src="'/nw1/saleItemImage/demoImg1.png'" class="h-[7.5rem] my-8" />
                </div>
                <div class="flex flex-col justify-center p-3 bg-white">
                    <slot name="saleItem" :itemInList="item"/>
                </div>
            </router-link>
        </div>
    </div>
</div>
</template>

<style scoped></style>
