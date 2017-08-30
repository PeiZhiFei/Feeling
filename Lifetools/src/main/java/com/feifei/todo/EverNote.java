package com.feifei.todo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;

import com.evernote.client.android.AsyncLinkedNoteStoreClient;
import com.evernote.client.android.EvernoteSession;
import com.evernote.client.android.EvernoteUtil;
import com.evernote.client.android.OnClientCallback;
import com.evernote.edam.type.LinkedNotebook;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.thrift.transport.TTransportException;
import com.feifei.util.DialogUtil;
import com.feifei.util.DialogView;
import com.feifei.util.LogUtil;
import com.feifei.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;



public class EverNote {
    private String mSelectedNotebookGuid;
    //todo 这里是生活小助手的
//    private static final String CONSUMER_KEY = "qweasd6886-2489";
//    private static final String CONSUMER_SECRET = "b1a32852c7ba97d2";
    private static final String CONSUMER_KEY = "qweasd6886-5802";
    private static final String CONSUMER_SECRET = "705d80b971552976";
    private static final EvernoteSession.EvernoteService EVERNOTE_SERVICE = EvernoteSession.EvernoteService.PRODUCTION;
    private EvernoteSession mEvernoteSession;
    private static final boolean SUPPORT_APP_LINKED_NOTEBOOKS = true;
    private static final String greenpre = "<span style=\"color:green;\">";
    private static final String greenaft = "</span>";
    private static final String enter = "<br/>";
    private static final String todoPre = "<en-todo checked=\"";
    private static final String todoAft = "\"/>";


    private static final String CREATERROR = "保存失败，再试试吧！";
    private static final String CREATEOK = "保存成功";
    Context context;

    public EverNote (Context context) {
        this.context = context;
        mEvernoteSession = EvernoteSession.getInstance (context, CONSUMER_KEY, CONSUMER_SECRET,
                EVERNOTE_SERVICE, SUPPORT_APP_LINKED_NOTEBOOKS);
    }

    public void author () {
        mEvernoteSession.authenticate (context);
    }

    //创建笔记
    public void saveNote (ArrayList<TODO> arrayList) {
        if (arrayList.size () > 0) {
            Note note = new Note ();
            note.setTitle ("简单TODO");
            StringBuffer stringBuffer = new StringBuffer ();
            for (TODO todo : arrayList) {
                //拼凑ENML格式笔记
                stringBuffer.append (todoPre + todo.isdone () + todoAft + greenpre + todo.getItem () + greenaft + enter);
            }
            LogUtil.log (stringBuffer.toString ());
            note.setContent (EvernoteUtil.NOTE_PREFIX + stringBuffer.toString () + EvernoteUtil.NOTE_SUFFIX);
            if (!mEvernoteSession.getAuthenticationResult ().isAppLinkedNotebook ()) {
                // 如果选择了笔记本才设置，非必须
                if (!TextUtils.isEmpty (mSelectedNotebookGuid)) {
                    note.setNotebookGuid (mSelectedNotebookGuid);
                }
                ToastUtil.toast (context, "正在保存……");
                try {
                    mEvernoteSession.getClientFactory ().createNoteStoreClient ()
                            .createNote (note, mNoteCreateCallback);
                } catch (TTransportException exception) {
                    ToastUtil.toast(context, CREATERROR);
                }
            } else {
                createNoteInAppLinkedNotebook (note, mNoteCreateCallback);
            }
        }
    }

    //创建笔记
    public void saveNote2 (ArrayList<NotifyTODO> arrayList) {
        if (arrayList.size () > 0) {
            Note note = new Note ();
            note.setTitle ("简单TODO");
            StringBuffer stringBuffer = new StringBuffer ();
            for (NotifyTODO todo : arrayList) {
                //拼凑ENML格式笔记
                stringBuffer.append ("<en-todo/> " + greenpre + todo.getItem () + greenaft + enter);
            }
            LogUtil.log(stringBuffer.toString());
            note.setContent (EvernoteUtil.NOTE_PREFIX + stringBuffer.toString () + EvernoteUtil.NOTE_SUFFIX);
            if (!mEvernoteSession.getAuthenticationResult ().isAppLinkedNotebook ()) {
                // 如果选择了笔记本才设置，非必须
                if (!TextUtils.isEmpty (mSelectedNotebookGuid)) {
                    note.setNotebookGuid (mSelectedNotebookGuid);
                }
                ToastUtil.toast (context, "正在保存……");
                try {
                    mEvernoteSession.getClientFactory ().createNoteStoreClient ()
                            .createNote (note, mNoteCreateCallback);
                } catch (TTransportException exception) {
                    ToastUtil.toast (context, CREATERROR);
                }
            } else {
                createNoteInAppLinkedNotebook (note, mNoteCreateCallback);
            }
        }
    }

    //创建笔记的结果回调
    private final OnClientCallback<Note> mNoteCreateCallback = new OnClientCallback<Note> () {
        @Override
        public void onSuccess (Note note) {
            ToastUtil.toast (context, CREATEOK);
        }

        @Override
        public void onException (Exception exception) {
            ToastUtil.toast (context, CREATERROR);
        }
    };

    //选择笔记本
    public void selectNotebook () {
        if (mEvernoteSession.isAppLinkedNotebook ()) {
            ToastUtil.toast (context, "Cannot select notebook: account can only access one linked notebook");
            return;
        }
        try {
            mEvernoteSession.getClientFactory ().createNoteStoreClient ()
                    .listNotebooks (new OnClientCallback<List<Notebook>> () {
//                        int mSelectedPos = -1;

                        @Override
                        public void onSuccess (final List<Notebook> notebooks) {
                            ArrayList<String> arrayList = new ArrayList<> ();
                            for (Notebook notebook2 : notebooks) {
                                arrayList.add (notebook2.getName ());
                            }

                            DialogUtil.dialogStyle0(context, "请选择笔记本", true, arrayList, new DialogView.DialogViewListener() {
                                @Override
                                public void onRightClick() {

                                }

                                @Override
                                public void onLeftClick() {

                                }

                                @Override
                                public void onListItemClick(int i, String s) {
                                    mSelectedNotebookGuid = notebooks.get(i).getGuid();
                                }

                                @Override
                                public void onButtonClick() {

                                }
                            });

//                            CharSequence[] names = new CharSequence[notebooks.size ()];
//                            int selected = -1;
//                            Notebook notebook = null;
//                            for (int index = 0; index < notebooks.size (); index++) {
//                                notebook = notebooks.get (index);
//                                names[index] = notebook.getName ();
//                                if (notebook.getGuid ().equals (mSelectedNotebookGuid)) {
//                                    selected = index;
//                                }
//                            }
//                            AlertDialog.Builder builder = new AlertDialog.Builder (context);
//                            builder.setSingleChoiceItems (names, selected,
//                                    new DialogInterface.OnClickListener () {
//                                        @Override
//                                        public void onClick (DialogInterface dialog, int which) {
//                                            mSelectedPos = which;
//                                        }
//                                    })
//                                    .setPositiveButton (com.evernote.sdk.R.string.ok,
//                                            new DialogInterface.OnClickListener () {
//                                                @Override
//                                                public void onClick (DialogInterface dialog,
//                                                                     int which) {
//                                                    if (mSelectedPos > -1) {
//                                                        mSelectedNotebookGuid = notebooks.get (
//                                                                mSelectedPos).getGuid ();
//                                                    }
//                                                    dialog.dismiss ();
//                                                }
//                                            }).create ().show ();
                        }

                        @Override
                        public void onException (Exception exception) {
                            ToastUtil.toast (context, "获取笔记本目录失败");
                        }
                    });
        } catch (TTransportException exception) {
            ToastUtil.toast (context, CREATERROR);
        }
    }


    //创建笔记
    protected void createNoteInAppLinkedNotebook (final Note note,
                                                  final OnClientCallback<Note> createNoteCallback) {
        ToastUtil.toast (context, "正在保存……");
        invokeOnAppLinkedNotebook (new OnClientCallback<Pair<AsyncLinkedNoteStoreClient, LinkedNotebook>> () {
            @Override
            public void onSuccess (final Pair<AsyncLinkedNoteStoreClient, LinkedNotebook> pair) {
                // Rely on the callback to dismiss the dialog
                pair.first.createNoteAsync (note, pair.second, createNoteCallback);
            }

            @Override
            public void onException (Exception exception) {
                ToastUtil.toast (context, CREATERROR);
            }
        });
    }

    protected void invokeOnAppLinkedNotebook (
            final OnClientCallback<Pair<AsyncLinkedNoteStoreClient, LinkedNotebook>> callback) {
        try {
            // We need to get the one and only linked notebook
            mEvernoteSession.getClientFactory ().createNoteStoreClient ()
                    .listLinkedNotebooks (new OnClientCallback<List<LinkedNotebook>> () {
                        @Override
                        public void onSuccess (List<LinkedNotebook> linkedNotebooks) {
                            // We should only have one linked notebook
                            if (linkedNotebooks.size () != 1) {
                                callback.onException (new Exception ("Not single linked notebook"));
                            } else {
                                final LinkedNotebook linkedNotebook = linkedNotebooks.get (0);
                                mEvernoteSession.getClientFactory ()
                                        .createLinkedNoteStoreClientAsync (linkedNotebook,
                                                new OnClientCallback<AsyncLinkedNoteStoreClient> () {
                                                    @Override
                                                    public void onSuccess (
                                                            AsyncLinkedNoteStoreClient asyncLinkedNoteStoreClient) {
                                                        // Finally create the note in the linked notebook
                                                        callback.onSuccess (new Pair<AsyncLinkedNoteStoreClient, LinkedNotebook> (
                                                                asyncLinkedNoteStoreClient,
                                                                linkedNotebook));
                                                    }

                                                    @Override
                                                    public void onException (Exception exception) {
                                                        callback.onException (exception);
                                                    }
                                                });
                            }
                        }

                        @Override
                        public void onException (Exception exception) {
                            callback.onException (exception);
                        }
                    });
        } catch (TTransportException exception) {
            callback.onException (exception);
        }
    }


}
