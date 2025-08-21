class BrokenBakery  {
    volatile int[] ticket;
    volatile int n;

    public BrokenBakery (int n) {
        ticket = new int[n];
        n = n;
        for (int i = 0; i < n; i++) {
            ticket[i] = 0;
        }
    }

    public void lock() {
        int id = (int) Thread.currentThread().getId();
        for (int j = 0; j < n; j++)
            if (ticket[j] > ticket[id])
                ticket[id] = ticket[j];
                ticket[id]++;
        for (int j = 0; j < n; j++) {
            while ((ticket[j] != 0) && ((ticket[j] < ticket[id])));
        }
    }
    public void unlock() {
        int id = (int) Thread.currentThread().getId();
        ticket[id] = 0;
    }
}

