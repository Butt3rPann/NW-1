<script setup>

const props = defineProps({
    items: {
        type: Array,
        required: true
    },
    view: {
        type: String,
        default: 'gallery',
        validator: (value) => ['gallery', 'list'].includes(value)
    },
    images: Array
})

const baseUrl = import.meta.env.VITE_APP_URL

</script>

<template>
    <div :class="view === 'gallery' ? 'grid grid-cols-2 md:grid-cols-4 gap-5 mx-auto' : 'flex flex-col gap-7'">
        <div v-for="item in items" :key="item.id" class="itbms-row shadow-[0_0.065rem_0.18rem_0_rgba(0,0,0,0.15)] rounded-md overflow-hidden hover:shadow-[0_0.08rem_0.4rem_rgba(0,0,0,0.15)] bg-white"
            :class="view === 'gallery' ? 'hover:scale-[1.01]' : 'flex flex-row'" >
            <router-link :to="{ name: 'SaleItemsDetail', params: { saleItemId: item.id } }">
                <div class="flex items-center justify-center bg-[#FAF6F5] h-34 lg:h-42 xl:h-50">
                    <p v-if="!images[item.id]" class="text-[#332A1E] text-sm lg:text-base xl:text-lg">No Picture</p>
                    <img v-else :src="`${baseUrl}/v1/files/${images[item.id]}`" class="h-18 md:h-18 lg:h-24 xl:h-30 my-6 lg:my-8" />
                </div>
                <div class="flex flex-col justify-center p-3 bg-white">
                    <slot name="saleItem" :itemInList="item"/>
                </div>
            </router-link>
        </div>
    </div>
</template>

<style scoped></style>
