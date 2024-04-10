<template>
  <Carousel :orientation="orientation as any" :plugins="[Autoplay({delay: delay})]">
    <CarouselContent>
      <CarouselItem v-for="item in items">
        <div v-if="item.isAlert">
          <DcLink :external="item.external" :link="item.link">
            <Alert :title="item.title"/>
          </DcLink>
        </div>
        <div v-else>
          <DcLink :external="item.external" :link="item.link"/>
        </div>
      </CarouselItem>
    </CarouselContent>
    <CarouselPrevious v-if="previous"/>
    <CarouselNext v-if="next"/>
  </Carousel>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import { Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious } from '@/components/ui/carousel'
import Autoplay from 'embla-carousel-autoplay'
import CalendarModel from 'echarts/types/src/coord/calendar/CalendarModel'
import Alert from '@/views/ui/alert'
import DcLink from '@/views/components/link/DcLink.vue'

export default defineComponent({
  name: 'DcCarousel',
  computed: {
    Autoplay()
    {
      return Autoplay
    }
  },
  components: {
    DcLink,
    Carousel, CarouselContent, CarouselItem, CarouselNext, CarouselPrevious,
    Alert
  },
  props: {
    autoPlay: {
      type: Boolean,
      default: true
    },
    delay: {
      type: Number,
      default: 2000
    },
    previous: {
      type: Boolean,
      default: false
    },
    next: {
      type: Boolean,
      default: false
    },
    orientation: {
      type: String,
      default: 'horizontal'
    },
    items: {
      type: Array as () => CalendarModel[],
      default: () => new Array<CalendarModel>()
    }
  }
})
</script>
