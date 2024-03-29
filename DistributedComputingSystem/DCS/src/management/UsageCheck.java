package management;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class UsageCheck implements Runnable {

	float FreeMem = 0;
	float TotalMem = 0;
	String Memory = "";

	private Thread update = null;

	public void startUpdate() {
		System.out.println("[UsageCheck.java] startUpdate()");
		if (update != null)
			return;

		this.update = new Thread(this);

		if (update.isAlive())
			return;

		update.start();
	}

	public void stopUpdate() {
		System.out.println("[UsageCheck.java] stopUpdate()");
		if (update != null) {
			if (update.isAlive()) {
				update.interrupt();
			}
		}
		update = null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("[UsageCheck.java] run()");
		while (!Thread.interrupted()) {
			OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

			try {

				for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
					
					method.setAccessible(true);
					
					if (method.getName().startsWith("getFreePhysicalMemorySize")
							&& Modifier.isPublic(method.getModifiers())) {
						
						Object value;
						try {
							value = method.invoke(operatingSystemMXBean);
						} catch (Exception e) {
							value = e;
						}
						FreeMem = ((float) ((long) value / 1024) / 1024) / 1024;
					
					}

					if (method.getName().startsWith("getTotalPhysicalMemorySize")
							&& Modifier.isPublic(method.getModifiers())) {
						Object value;
						try {
							value = method.invoke(operatingSystemMXBean);
						} catch (Exception e) {
							value = e;
						}
						
						TotalMem = ((float) ((long) value / 1024) / 1024) / 1024;
						Memory = String.format("%.2f", TotalMem);						
						WorkerResManager.getInstance().setMEMORY_CAPA(Memory);
						Memory = String.format("%.2f", TotalMem - FreeMem);						
						WorkerResManager.getInstance().setMEMORY_USAGE(Memory);

					}

					if (method.getName().startsWith("getSystemCpuLoad") && Modifier.isPublic(method.getModifiers())) {
						Object value;
						try {
							value = method.invoke(operatingSystemMXBean);
						} catch (Exception e) {
							value = e;
						}
						String CPU = String.format("%.2f", (double) value * 100);
						WorkerResManager.getInstance().setCPU_USAGE(CPU);	
					}

				}
				Thread.sleep(500);

			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("[UsageCheck.java] Err");
			}
		}

	}

}
