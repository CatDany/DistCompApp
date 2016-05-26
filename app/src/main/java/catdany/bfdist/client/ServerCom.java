package catdany.bfdist.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

import catdany.bfdist.BFHelper;
import catdany.bfdist.log.BFLog;

public class ServerCom implements Runnable
{
	public final Socket socket;
	
	private Thread comThread;
	private BufferedReader in;
	private PrintWriter out;
	
	private Thread syracuseThread;
	private SyracuseSolver syracuseSolver;
	
	public ServerCom(Socket socket)
	{
		this.socket = socket;
		try
		{
			this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), BFHelper.charset));
			this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), BFHelper.charset), true);
			this.comThread = new Thread(this, "Client-ServerCom");
			comThread.start();
		}
		catch (IOException t)
		{
			BFLog.t(t);
			BFLog.exit("Couldn't get I/O from server.");
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			String read;
			while ((read = in.readLine()) != null)
			{
				BFLog.d("Received message from server: %s", read);
				if (read.startsWith("SPSTART"))
				{
					if (syracuseThread != null)
					{
						syracuseThread.interrupt();
						syracuseSolver = null;
					}
					String[] split = read.split(" ");
					BigInteger start = new BigInteger(split[2]);
					BigInteger end = new BigInteger(split[2]).add(new BigInteger(split[3]));
					syracuseSolver = new SyracuseSolver(Long.parseLong(split[1]), start, end, this);
					syracuseThread = new Thread(syracuseSolver, "Syracuse-Solver");
					syracuseThread.setPriority(9);//XXX: SyracuseSolver Thread Priority
					syracuseThread.start();
					BFLog.i("Started Solver on an interval [%s...%s]", split[2], end);
				}
				else if (read.equals("SHUTDOWN"))
				{
					BFLog.i("Server has been stopped.");
					break;
				}
			}
			throw new RuntimeException("readLine() = null");
		}
		catch (Exception t)
		{
			syracuseSolver.interrupt();
			BFLog.t(t);
			BFLog.exit("Error occurred while communicating with server.");
		}
	}
	
	/**
	 * Send a message to server
	 * @param msg
	 */
	public void sendToServer(String msg)
	{
		out.println(msg);
		BFLog.d("Sent '%s' to server", msg);
	}

	public void stop() {
		syracuseSolver.interrupt();
		try {
			socket.close();
		} catch (IOException t) {
			BFLog.t(t);
		}
	}
}
