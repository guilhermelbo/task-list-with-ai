---
name: nextjs-patterns
description: >
  Use this skill when building Next.js 14+ applications with the App Router, Server
  Components, streaming, parallel routes, intercepting routes, or Server Actions.
  Trigger for questions about Next.js architecture, data fetching strategies, caching
  with revalidation tags, migrating from Pages Router to App Router, implementing
  modals with intercepting routes, parallel dashboard panels, or building full-stack
  features with Server Actions. Also trigger for metadata/SEO generation, route handlers,
  and Suspense-based streaming patterns.
---

# Next.js App Router Patterns

> When this skill is active, use this knowledge as specialized reference for Next.js 14+ App Router architecture, Server Components, streaming, parallel routes, and advanced data fetching patterns.

---

## Use this skill when

- Building new Next.js applications with App Router
- Migrating from Pages Router to App Router
- Implementing Server Components and streaming
- Setting up parallel and intercepting routes
- Optimizing data fetching and caching
- Building full-stack features with Server Actions

---

## Core Concepts

### 1. Rendering Modes

| Mode | Where | When to Use |
|------|-------|-------------|
| **Server Components** | Server only | Data fetching, heavy computation, secrets |
| **Client Components** | Browser | Interactivity, hooks, browser APIs |
| **Static** | Build time | Content that rarely changes |
| **Dynamic** | Request time | Personalized or real-time data |
| **Streaming** | Progressive | Large pages, slow data sources |

### 2. File Conventions

```
app/
├── layout.tsx       # Shared UI wrapper
├── page.tsx         # Route UI
├── loading.tsx      # Loading UI (Suspense)
├── error.tsx        # Error boundary
├── not-found.tsx    # 404 UI
├── route.ts         # API endpoint
├── template.tsx     # Re-mounted layout
├── default.tsx      # Parallel route fallback
└── opengraph-image.tsx  # OG image generation
```

---

## Quick Start

```typescript
// app/layout.tsx
import { Inter } from 'next/font/google'
import { Providers } from './providers'

const inter = Inter({ subsets: ['latin'] })

export const metadata = {
  title: { default: 'My App', template: '%s | My App' },
  description: 'Built with Next.js App Router',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={inter.className}>
        <Providers>{children}</Providers>
      </body>
    </html>
  )
}

// app/page.tsx - Server Component by default
async function getProducts() {
  const res = await fetch('https://api.example.com/products', {
    next: { revalidate: 3600 }, // ISR: revalidate every hour
  })
  return res.json()
}

export default async function HomePage() {
  const products = await getProducts()

  return (
    <main>
      <h1>Products</h1>
      <ProductGrid products={products} />
    </main>
  )
}
```

---

## Patterns

### Pattern 1: Server Components with Data Fetching

```typescript
// app/products/page.tsx
import { Suspense } from 'react'
import { ProductList, ProductListSkeleton } from '@/components/products'
import { FilterSidebar } from '@/components/filters'

interface SearchParams {
  category?: string
  sort?: 'price' | 'name' | 'date'
  page?: string
}

export default async function ProductsPage({
  searchParams,
}: {
  searchParams: Promise<SearchParams>
}) {
  const params = await searchParams

  return (
    <div className="flex gap-8">
      <FilterSidebar />
      <Suspense
        key={JSON.stringify(params)}
        fallback={<ProductListSkeleton />}
      >
        <ProductList
          category={params.category}
          sort={params.sort}
          page={Number(params.page) || 1}
        />
      </Suspense>
    </div>
  )
}
```

### Pattern 2: Client Components with 'use client'

```typescript
// components/products/AddToCartButton.tsx
'use client'

import { useState, useTransition } from 'react'
import { addToCart } from '@/app/actions/cart'

export function AddToCartButton({ productId }: { productId: string }) {
  const [isPending, startTransition] = useTransition()
  const [error, setError] = useState<string | null>(null)

  const handleClick = () => {
    setError(null)
    startTransition(async () => {
      const result = await addToCart(productId)
      if (result.error) {
        setError(result.error)
      }
    })
  }

  return (
    <div>
      <button
        onClick={handleClick}
        disabled={isPending}
        className="btn-primary"
      >
        {isPending ? 'Adding...' : 'Add to Cart'}
      </button>
      {error && <p className="text-red-500 text-sm">{error}</p>}
    </div>
  )
}
```

### Pattern 3: Server Actions

```typescript
// app/actions/cart.ts
'use server'

import { revalidateTag } from 'next/cache'
import { cookies } from 'next/headers'
import { redirect } from 'next/navigation'

export async function addToCart(productId: string) {
  const cookieStore = await cookies()
  const sessionId = cookieStore.get('session')?.value

  if (!sessionId) {
    redirect('/login')
  }

  try {
    await db.cart.upsert({
      where: { sessionId_productId: { sessionId, productId } },
      update: { quantity: { increment: 1 } },
      create: { sessionId, productId, quantity: 1 },
    })

    revalidateTag('cart')
    return { success: true }
  } catch (error) {
    return { error: 'Failed to add item to cart' }
  }
}
```

### Pattern 4: Parallel Routes

```typescript
// app/dashboard/layout.tsx
export default function DashboardLayout({
  children,
  analytics,
  team,
}: {
  children: React.ReactNode
  analytics: React.ReactNode
  team: React.ReactNode
}) {
  return (
    <div className="dashboard-grid">
      <main>{children}</main>
      <aside className="analytics-panel">{analytics}</aside>
      <aside className="team-panel">{team}</aside>
    </div>
  )
}
```

### Pattern 5: Intercepting Routes (Modal Pattern)

```typescript
// File structure for photo modal
// app/
// ├── @modal/
// │   ├── (.)photos/[id]/page.tsx  # Intercept
// │   └── default.tsx
// ├── photos/
// │   └── [id]/page.tsx            # Full page
// └── layout.tsx

// app/@modal/(.)photos/[id]/page.tsx
import { Modal } from '@/components/Modal'
import { PhotoDetail } from '@/components/PhotoDetail'

export default async function PhotoModal({
  params,
}: {
  params: Promise<{ id: string }>
}) {
  const { id } = await params
  const photo = await getPhoto(id)

  return (
    <Modal>
      <PhotoDetail photo={photo} />
    </Modal>
  )
}
```

### Pattern 6: Streaming with Suspense

```typescript
// app/product/[id]/page.tsx
import { Suspense } from 'react'

export default async function ProductPage({
  params,
}: {
  params: Promise<{ id: string }>
}) {
  const { id } = await params
  const product = await getProduct(id)

  return (
    <div>
      {/* Immediate render */}
      <ProductHeader product={product} />

      {/* Stream in reviews */}
      <Suspense fallback={<ReviewsSkeleton />}>
        <Reviews productId={id} />
      </Suspense>

      {/* Stream in recommendations */}
      <Suspense fallback={<RecommendationsSkeleton />}>
        <Recommendations productId={id} />
      </Suspense>
    </div>
  )
}
```

### Pattern 7: Route Handlers (API Routes)

```typescript
// app/api/products/route.ts
import { NextRequest, NextResponse } from 'next/server'

export async function GET(request: NextRequest) {
  const searchParams = request.nextUrl.searchParams
  const category = searchParams.get('category')

  const products = await db.product.findMany({
    where: category ? { category } : undefined,
    take: 20,
  })

  return NextResponse.json(products)
}

export async function POST(request: NextRequest) {
  const body = await request.json()
  const product = await db.product.create({ data: body })
  return NextResponse.json(product, { status: 201 })
}
```

### Pattern 8: Metadata and SEO

```typescript
// app/products/[slug]/page.tsx
import { Metadata } from 'next'
import { notFound } from 'next/navigation'

type Props = { params: Promise<{ slug: string }> }

export async function generateMetadata({ params }: Props): Promise<Metadata> {
  const { slug } = await params
  const product = await getProduct(slug)

  if (!product) return {}

  return {
    title: product.name,
    description: product.description,
    openGraph: {
      title: product.name,
      description: product.description,
      images: [{ url: product.image, width: 1200, height: 630 }],
    },
  }
}
```

---

## Caching Strategies

```typescript
// No cache (always fresh)
fetch(url, { cache: 'no-store' })

// Cache forever (static)
fetch(url, { cache: 'force-cache' })

// ISR - revalidate after 60 seconds
fetch(url, { next: { revalidate: 60 } })

// Tag-based invalidation
fetch(url, { next: { tags: ['products'] } })

// Invalidate via Server Action
'use server'
import { revalidateTag, revalidatePath } from 'next/cache'

export async function updateProduct(id: string, data: ProductData) {
  await db.product.update({ where: { id }, data })
  revalidateTag('products')
  revalidatePath('/products')
}
```

---

## Best Practices

### Do's
- **Start with Server Components** - Add 'use client' only when needed
- **Colocate data fetching** - Fetch data where it's used
- **Use Suspense boundaries** - Enable streaming for slow data
- **Leverage parallel routes** - Independent loading states
- **Use Server Actions** - For mutations with progressive enhancement

### Don'ts
- **Don't pass serializable data** - Server → Client boundary limitations
- **Don't use hooks in Server Components** - No useState, useEffect
- **Don't fetch in Client Components** - Use Server Components or React Query
- **Don't over-nest layouts** - Each layout adds to the component tree
- **Don't ignore loading states** - Always provide loading.tsx or Suspense
