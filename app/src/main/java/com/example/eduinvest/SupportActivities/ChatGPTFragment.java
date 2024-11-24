package com.example.eduinvest.SupportActivities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import com.example.eduinvest.R;
import com.example.eduinvest.adapters.MessageAdapter;
import com.example.eduinvest.models.MessageModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatGPTFragment extends Fragment {

    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton sendButton;
    List<MessageModel> messageList;
    MessageAdapter messageAdapter;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    public ChatGPTFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_g_p_t, container, false);
        messageList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recycler_view);
        messageEditText = view.findViewById(R.id.message_edit_text);
        sendButton = view.findViewById(R.id.send_btn);

        // setup recycler view
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        sendButton.setOnClickListener((v) -> {
            String question = messageEditText.getText().toString().trim();
            addToChat(question, MessageModel.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);
        });
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    void addToChat(String message, String sentBy) {
        requireActivity().runOnUiThread(() -> {
            messageList.add(new MessageModel(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        requireActivity().runOnUiThread(() -> {
            if (!messageList.isEmpty()) {
                messageList.remove(messageList.size() - 1);
            }
            addToChat(response, MessageModel.SENT_BY_BOT);
        });
    }

    void callAPI(String question) {
        messageList.add(new MessageModel("Typing...", MessageModel.SENT_BY_BOT));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo-instruct");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 2000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            Log.e("JSONException", e.toString());
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer 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") // Sử dụng khóa API an toàn hơn
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body() != null ? response.body().string() : null;
                    if (responseBody != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseBody);
                            JSONArray jsonArray = jsonObject.getJSONArray("choices");
                            String result = jsonArray.getJSONObject(0).getString("text");
                            addResponse(result.trim());
                        } catch (JSONException e) {
                            Log.e("JSONException", e.toString());
                        }
                    } else {
                        addResponse("Failed to load response: empty body");
                    }
                } else {
                    addResponse("Failed to load response due to " + response.message());
                }
            }
        });
    }
}
