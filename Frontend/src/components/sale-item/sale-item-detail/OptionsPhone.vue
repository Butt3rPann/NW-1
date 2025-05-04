<script setup>
const props = defineProps({
  phones: {
    type: Array,
    required: true,
  },
  selectedIndex: {
    type: Number,
    required: true,
  },
})

const emit = defineEmits(['update:selectedIndex']);

const updateSelected = (index) => {
  emit('update:selectedIndex', index);
}

console.log(props.phones)
</script>

<template>
  <div>
    <div class="flex mt-[1.3vw] items-center">
      <button @click="updateSelected(selectedIndex === 0 ? phones.length - 1 : selectedIndex - 1)"
        class="text-[#6F879C] text-[2.5vw] w-[4vw] h-[4vw] rounded-full flex items-center justify-center transition duration-200 hover:bg-[#6F879C]/10">
      <
      </button>
      <div class="flex w-[34vw] justify-around">
        <div v-for="(thumbnail, index) in phones" :key="index"
          class="bg-[#F0EDEC] w-[7.5vw] h-[8.5vw] p-[1.3vw] m-[0.4vw] rounded-xl flex items-center justify-center"
          :class="{ 'border-[0.25vw] border-[#6F879C]': selectedIndex === index }" @click="updateSelected(index)">
          <img :src="thumbnail" :alt="`Phone ${index}`" class="h-[4.3vw] object-cover scale-125" />
        </div>
      </div>
      <button @click="updateSelected(selectedIndex === phones.length - 1 ? 0 : selectedIndex + 1)"
        class="text-[#6F879C] text-[2.5vw] w-[4vw] h-[4vw] rounded-full flex items-center justify-center transition duration-200 hover:bg-[#6F879C]/10">
      >
      </button>
    </div>
  </div>
</template>

<style scoped></style>