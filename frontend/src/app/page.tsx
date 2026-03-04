import TaskList from "@/components/tasks/TaskList";

export default function Home() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-zinc-50 font-sans dark:bg-zinc-950 p-4 sm:p-8">
      <main className="w-full max-w-4xl mx-auto flex flex-col gap-8">
        <header className="text-center sm:text-left mb-8">
          <h1 className="text-4xl font-extrabold tracking-tight lg:text-5xl text-zinc-900 dark:text-zinc-50">
            Task List
          </h1>
          <p className="mt-2 text-lg text-zinc-500 dark:text-zinc-400">
            Spec-Driven DDD Clean Architecture
          </p>
        </header>

        <TaskList />
      </main>
    </div>
  );
}
