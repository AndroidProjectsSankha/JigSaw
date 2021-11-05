package com.android.Jumble;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogResult {
	public interface Command {
		public void execute();

		public static final Command No_Operation = new Command() {
			public void execute() {
			}
		};
	}
	
	public DialogResult.Command YesCommand = new Command() {
		
		public void execute() {
			// TODO Auto-generated method stub
			
		}
	};
	
	public DialogResult.Command NoCommand = new Command() {
		
		public void execute() {
			// TODO Auto-generated method stub
			
		}
	};

	public static class CommandWrapper implements
			DialogInterface.OnClickListener {

		private Command command;

		public CommandWrapper(Command command) {
			this.command = command;
		}

		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			command.execute();
		}
	}

	private static final CommandWrapper DISMISS = new CommandWrapper(
			Command.No_Operation);

	public static AlertDialog createDialog(final Context context,
			final String msg, final Command yesCommand, final Command noCommand) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(true);
		// builder.setIcon(R.drawable.dialog_question);
		builder.setTitle(msg);
		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton("Oui", new CommandWrapper(yesCommand));
		builder.setNegativeButton("Non", new CommandWrapper(noCommand));
		return builder.create();
	}

	public static void show(final Context context, final String msg,
			final Command OKCommand) {
		DialogResult
				.createDialog(context, msg, OKCommand, Command.No_Operation)
				.show();
	}

	public static void show(final Context context, final String msg,
			final Command yesCommand, final Command noCommand) {
		DialogResult.createDialog(context, msg, yesCommand, noCommand).show();
	}
}